#!/bin/sh

#SBATCH --mail-type=ALL
#SBATCH --mail-user=breeze.du@gmail.com
#SBATCH -c 16
#SBATCH --mem-per-cpu=8G
#SBATCH --nodes=4
#SBATCH --job-name=PyStanSavedModel

/dscrhome/gd44/jeffapps/bin/python2.7 0124_pystan_AllExon_SavedModel.py