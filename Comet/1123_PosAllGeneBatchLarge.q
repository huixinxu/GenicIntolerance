#!/bin/sh

#SBATCH --mail-type=ALL
#SBATCH --mail-user=breeze.du@gmail.com
#SBATCH -p compute
#SBATCH -c 24
#SBATCH --mem-per-cpu=4G
#SBATCH --ntasks=24
#SBATCH --job-name=AllGenePoReg
#SBATCH -t 47:50:00

module load R
srun R CMD BATCH ./1123_PoissonRegression_AllGene4000ite.R