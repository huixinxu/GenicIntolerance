## ShuaiQi's Project
## Date 12-30-2015
## Aim: Try 100,000 Genes
## @ authors: SQ
## Data source: 
## Models:
## Parameters:


## Data source and copyright?
## Read in table from local hard drive:
## setup the working directory to where exon_level_process_v2.txt locates

##########################################
## Part One Read in the data
## create the tables
##########################################


table <- read.table("exon_level_process_v2.txt")

## table <- read.table("D:/GitHub/exon_level_process_v2.txt")
## table<-read.table("C:/Users/shuaiqi/Desktop/duke/Andrew/data/for_asa/other_stuff/exon_level_process_v3.txt")


## assign column names
colnames(table) <- c("chr", "gene", "dom", "subdom", "exon", "gene.dom", 
             "gene.dom.subdom", 
             "envarp",    # pass
             "envarpf",   # pass functional
             "envarpfr",  # pass functional rare
             "emutr")     # mutation rate 


## attract data from the original table??
table<-within(table,envarpfc<-envarpf-envarpfr)#y

table<-within(table,gene<-factor(gene))

table<-within(table,gene.dom<-factor(gene.dom))

table<-within(table,gene.dom.subdom<-factor(gene.dom.subdom))



##
## table<-table[1:500,]

#for the use of counting number of gene
sumenvarp<-aggregate(table$envarp, by=list(Category=table$gene), FUN=sum)

sumenvarpfc<-aggregate(table$envarpfc, by=list(Category=table$gene), FUN=sum)[,2]

## simplify table1
table1<-data.frame(cbind(sumenvarp,sumenvarpfc))


## assign column names to table1
colnames(table1)<-c("gene","sumenvarp","sumenvarpfc")



#######################################################
## Part Two
## Call Stan to do the simutation
#######################################################


#install.packages("rstan")

library("rstan")

rstan_options(auto_write = TRUE)
options(mc.cores = parallel::detectCores())

##gene_code



######################
# init the parameters
######################
N<-dim(table)[1]
J<-dim(table1)[1]                       #number of genes in table1
gene<-as.numeric(table$gene)
genelevel<-length(unique(gene))
index<-match(gene, unique(gene)) 

M1_table<-list( J=J, y=table1$sumenvarpfc,
                x=table1$sumenvarp,gene=c(1:length(table1$sumenvarpfc)))


## fit rstan()

## fit the model
fit0 <- stan(file = "possion.simpgene.rstan.stan")

## fit the model with data
fit1 <- stan(fit=fit0, data = M1_table, 
				iter = 400, 
				chains=4)


## fit1 <- stan(model_code = gene_code, data=M1_table, iter=200, chains=4)

print(fit1, "a")

intercept<-extract(fit1,"a")

write.table(intercept, "1131_fit1_Allgene100ite.txt", sep="\t")

print(fit1, "beta")

###########
## print(fit1, "a")
## print (fit1, "beta")


answer1<-extract(fit1, permuted = TRUE)
effect<-answer1$a
write.table(effect, "1231_Allgene_effectstan100.txt", sep="\t")


## check convergence 
pdf("1231_Allgene_traceplot100.pdf")
traceplot(fit1,pars=c("a"))
traceplot(fit1, pars=c("beta", "beta"))
dev.off()

##################
## END 
################## 
