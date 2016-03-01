#!/bin/sh

#SBATCH --mail-type=ALL
#SBATCH --mail-user=jeff.du@duke.edu
#SBATCH -c 16
#SBATCH --mem-per-cpu=8G
#SBATCH --nodes=8
#SBATCH --job-name=01262_100kIteration

R CMD BATCH ./0126_2_PoissonRegression_AllG100k.R