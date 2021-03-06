package coursera_java_duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class D0303_checkingHomHet{
	
	
	public static void main(String[] args) throws FileNotFoundException{
		
		
		//1st readin text file from local dick.
		//D:\Dropbox\PhD Project BioStats\Homozygosity Mutations\als\LizDeidentified_151002\LizDeidentified_151002
		String routine = "D:/Dropbox/PhD Project BioStats/Homozygosity Mutations/als/LizDeidentified_151002/LizDeidentified_151002/";
		
		//initial names for each documents, there are 12 of them together.
		String[] doc = new String[12];
		
		//only two documents for test code:
		//doc[0] = "gene_samp_matrix_high_LOF_het";
		//doc[1] = "gene_samp_matrix_high_LOF_hom";
		
		/**/
		doc[0] = "gene_samp_matrix_high_LOF_het";
		doc[1] = "gene_samp_matrix_high_LOF_hom";
		doc[2] = "gene_samp_matrix_high_not_benign_het";
		doc[3] = "gene_samp_matrix_high_not_benign_hom";
		
		doc[4] = "gene_samp_matrix_high_not_syn_het";
		doc[5] = "gene_samp_matrix_high_not_syn_hom";
		doc[6] = "gene_samp_matrix_low_LOF_het";
		doc[7] = "gene_samp_matrix_low_LOF_hom";
		
		doc[8] = "gene_samp_matrix_low_not_benign_het";
		doc[9] = "gene_samp_matrix_low_not_benign_hom";
		doc[10] = "gene_samp_matrix_low_not_syn_het";
		doc[11] = "gene_samp_matrix_low_not_syn_hom";
		/*****/
		
		
		//2nd initiate an ArrayList of genes:
		ArrayList<geneData> geneList = new ArrayList<geneData>();
		
		//for each document, check how many genes, how many 1s, how man 2s, how many NAS patients, and how many Controls.
		for(int i=0; i<doc.length; i++){
			
			//append .txt to each document.
			doc[i] += ".txt";
			
			//use a Scanner to 'scan' the document.
			Scanner read_in = new Scanner( new File(routine + doc[i]));
			
			//call checkEachDoc() method, to analysis the document. According to the data readed in from each docs
			//update the homoList and heteList of each gene in the geneList.
			geneList = checkEachDoc(read_in, doc[i], geneList);
			
			//close the read_in doc.
			read_in.close();
		
		}//end for loop;
		
		
		
		//3rd, statistic the n1 and n2, update Pi2[] for each gene.
		//check the # of genes in the geneList;
		
		ArrayList<Double> sampleMean = new ArrayList<Double>();
		ArrayList<Double> controlMean = new ArrayList<Double>();
		
		//set sample mean and control mean;
		for(int i=0; i<geneList.size(); i++){
			geneList.get(i).setControlMean();
			geneList.get(i).setSampleMean();
		}
		
		//assign sample mean and control mean to sampleMean arrayList and controlMean arrayList;
		//also, calculate the control-variance and sample-variance, after we get the sample-mean and control-mean for each gene;
		for(int i=0; i<geneList.size(); i++){
			sampleMean.add( geneList.get(i).getSampleMean() );
			controlMean.add( geneList.get(i).getControlMean());
			
			geneList.get(i).calControlVar();
			geneList.get(i).calSampleVar();
		}
		
		
		System.out.println("\n\n There are " + geneList.size() + " gene objects in the arrayList.");
		System.out.println("Printout first 50 control means:");
		for(int i=0; i<200; i++){
			
			if(i%5 == 0) System.out.println();
			System.out.print( controlMean.get(i) + "\t");
			
		}
		
		System.out.println("\n\n Printout sample-variance");
		for(int i=0; i<50; i++){
			
			if(i%5 == 0) System.out.println();
			System.out.print( geneList.get(i).getSampleVar() + "\t");
			
		}
		
		System.out.println("\n\n Printout control-variance:");
		for(int i=0; i<200; i++){
			
			if(i%5 == 0) System.out.println();
			System.out.print(geneList.get(i).getControlVar() + "\t");
			
		}
		
		
		
		//4th, calculate Tj[] for each gene
		ArrayList<Double> TjArrayList = new ArrayList<Double>();
		for(int i =0; i<geneList.size(); i++){
			
			double Tj_sum = 0.0;
			for(int j=0; j<geneList.get(i).getControlList().size(); j++){
				
				Tj_sum += geneList.get(i).getControlList().get(j);
			}
			
			TjArrayList.add(Tj_sum/geneList.get(i).getControlVar());
		}
		
		//printout Tj[]
		System.out.println("\n\n Printout Tj ArrayList:");
		
		for(int i=0; i<200; i++){
			
			if(i%5 == 0) System.out.println();
			System.out.print(TjArrayList.get(i) + "\t");
		}
		
		
		
		//
		System.out.println("\n\n Print control mean == 0 gene names: " + geneList.size());
		int zeroMeans = 0;
		for(int i=0; i<geneList.size(); i++){			
			
			if( Double.compare(geneList.get(i).getControlMean(), 0.0) == 0.0){ 
				System.out.print(geneList.get(i).getName() + "\t");
				zeroMeans += 1;
				
				if(zeroMeans%20 == 0) System.out.println();
			}
			
		}
		
		
		System.out.println("There are " + zeroMeans + " zero Means among all genes.");
		
	}//end main()
	

	/**********************
	 * checkEachDoc() method, to do the analysis work.
	 * @param read_in
	 * @param doc
	 * @param geneList 
	 * @return 
	 */
	private static ArrayList<geneData> checkEachDoc(Scanner read_in, String doc, ArrayList<geneData> geneList) {
		// TODO Auto-generated method stub
		
		//the first line of the document shows how many genes are there
		String first_line = read_in.nextLine();
		
		//split the first line string, with '\t'
		String[] geneStr = first_line.split("\t");
		
		
		//add every gene name to geneList, add 0 to correlate gene index;
		//the first string in geneStr[] array is "sample", so we ignore this one later;
		for(int i=0; i<geneStr.length; i++){
			
			//System.out.println("There are " + geneList.size() + " genes in the geneList.");
			if(geneList.size()< geneStr.length){
				
				//according to the name of each gene in the geneStr array, 
				//creat a geneData with empty arrayLists and store the geneData object in geneList.
				geneData currGene = new geneData(geneStr[i]);
			
				geneList.add(currGene);
				
			}//end if geneList is empty condition;
		
		}//end for i<geneStr.length loop;
		
		
		
		//print out informations from the text document;
		System.out.println("\nFor document: " + doc);		
		System.out.println("There are " + geneStr.length + " genes.");
		
		//initialize ones, twos, NASs, and Controls

		int NAS = 0;
		int Control = 0;

		
		//each line starts with NAS like "1899-NAS-1754", or Control like "CONTROL-10"
		while(read_in.hasNextLine()){
			
			
			String currLine = read_in.nextLine();
			String[] lineSplit = currLine.split("\t");
			//System.out.print("  " + lineSplit[0]);
			
			String sample = ""; 		//here sample = "control" or "sample";
			
			//Check the first world of each line, see if it contains "control", "nas"
			if(lineSplit[0].contains("CONTROL")){
				
					Control ++;
					sample = "Control";
				
				} else if(lineSplit[0].contains("NAS")){
					
					NAS ++;
					sample = "Sample";
				
				}//end if-else conditions;
				
			for(int i=1; i<lineSplit.length; i++){
				
				//parse the string into integer, then add the integer to geneData.sampleList;
				int num = Integer.parseInt(lineSplit[i]);
				
				if(sample == "Control")
					geneList.get(i-1).setControlList(num);
				
				else if (sample == "Sample")
					geneList.get(i-1).setSampleList(num);
			
			}

			
		}// end while loop;
				
		System.out.println("\n ***************************\n");
		System.out.println("There are " + geneList.size() + " genes.");
		System.out.println("Controls: " + Control + ", NAS: " + NAS);		
		
		return geneList;
		
	}//end of checkEachDoc() method;
	
	
}//ee

