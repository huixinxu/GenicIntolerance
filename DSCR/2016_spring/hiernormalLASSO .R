hiernormalLASSO<-"
data{ #get the data set 
int<lower=0> N;   # number of exon level
int<lower=0> J;   # number of gene level
int <lower=1,upper=J> gene[N];  #index of gene
int <lower=1,upper=N> exon[N];  #index of exon 
vector[N] xij;   #x at exon level
vector[N] yij; #y at exon level
}
parameters{ #specify the parameter we want to know 
	real lambda;#penalty 
	real beta;  #common slope for the exon level
	real mu;      #common intercept for the exon level
	vector[N] aij; #random intercept for the exon level
	real <lower=0> sigma_aj[J];  #variance of intercept at exon level 
	vector[J] aj; #random intercept for the gene level 
	real <lower=0> sigma_a;  #variance of intercept at gene level
	real <lower=0> sigma2; #variance of yij
}
transformed parameters{ #specify the model we will use 
}
model { #give the prior distribution 
   vector[N] lambdaij; #exon level model
   for (i in 1:N)
      lambdaij[i] <- mu+beta*xij[i]+aij[exon[i]]+aj[gene[i]];#specify the gene group
   lambda~ gamma(0.001,1);
   beta ~normal(0,1);
   sigma2 ~ inv_gamma(0.01,0.01); #inverse_gamma for matain normal conjugacy
   sigma_a ~ uniform(0,10);
   sigma_aj ~ uniform(0,20); #inverse_gamma for matain normal conjugacy
   aij~  double_exponential(0,sqrt(sigma2)/lambda);#lasso  laplace prior 
   yij ~ normal(lambdaij,sqrt(sigma2));
   aj ~ normal(0, sigma_a);
   for (i in 1:N){  
       aij[i]~ normal(0,sigma_aj[gene[i]]);
       increment_log_prob(-lambda*fabs(aij[i]));  #LASSO  fabs() in #unit_vector_constrain

      }             
}             
"
library("rstan")
J<-dim(table1)[1] #gene number 
N<-dim(table)[1]  #exon number 
xij=c(table$envarp)
xj=table1$sumenvarp
yij=table$envarpfc
yj=table1$sumenvarpfc
gene<-as.numeric(table$gene) #list
genelevel<-length(unique(gene)) #number
indexg<-match(gene, unique(gene))  #list
exon<-c(1:length(table$envarpfc))
M1_table<-list(N=N, J=J, xij=xij,
yij=yij,gene=indexg, exon=exon)
control=list(adapt_delta=0.99,max_treedepth=12)
fitlasso<-stan(model_code=hiernormalLASSO, data=M1_table,iter=20000,warmup=19000,chains=4)



#plot the density of parameters
#sigma_aj,sigma_a,sigma^2
answer<-extract(fitlasso,permuted=TRUE)
plotdesvar<-function(length){
	pdf(file = "lasso variance density plot")
	for (i in 1:length){
		plot(density(answer$sigma_aj[,i]),main=c("density plot of exon-level variance",i))
		}
	plot(density(answer$sigma_a),main="density plot of sigma_a")
	plot(density(answer$sigma2),main="density plot of sigma^2")
    dev.off()
    
}
plotdesvar(J)

#aij,beta,mu
plotdesint<-function(N){
	pdf(file = "lasso aij,mu,beta density plot")
	for (i in 1:N){
		plot(density(answer$aij[,i]),main=c("density plot of exon-level intercept",i))
		}
    plot(density(answer$beta),main="density plot of beta")
	plot(density(answer$mu),main="density plot of mu")	
    dev.off()
    
}
plotdesint(N)

#aj 
plotdes<-function(length){
	pdf(file = "lasso aj density plot")
	for (i in 1:length){
		plot(density(answer$aj[,i]),main=c("density plot of gene-level variance",i))
		}
    dev.off()
    
}
plotdes(J)

