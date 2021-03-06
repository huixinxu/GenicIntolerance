data{   #get the data we have

int <lower=0> J; #number of gene level 
int <lower=1,upper=J> gene[J];
vector[J] x;  #x
int <lower=0> y[J] ; #y
}
parameters{ #specify the parameter we want to know 
vector[J] a;  #random intercept when gene is the level 
real <lower=0> sigma_a;  #variance of intercept

real beta;    #common slope;
}
transformed parameters{ #specify the model we will use 
vector[J] lambda;
for (i in 1:J) 
     lambda[i] <- beta*x[i]+a[gene[i]];#specify the group
}
model { #give the prior distribution
  beta ~ normal(0,10);
  a ~ normal(0, sigma_a);
  y ~ poisson_log(lambda); #y and y_hat should have same type 
}