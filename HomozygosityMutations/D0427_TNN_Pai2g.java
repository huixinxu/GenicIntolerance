package coursera_java_duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class D0427_TNN_Pai2g {
	
	/***************
	 * Take one gene, SAMD11 for example:
	 * 
	 * Get all the mutation site within gene SAMD11, also delete those not on any Exons; 
	 * get the Allele count over population size AC/60,706;
	 * since the real number is too small, so get the log(AC/60,706)
	 * 
	 */
	
	
	/**********
	 * 1st
	 * From CCDS dataset, get gene frame/regions for each gene, 
	 * save them to a txt document with the same name as the gene name plus gene_frame;
	 * 
	 * i.e. for gene TNN the gene frame txt document would be TNN_gene_frame.txt
	 * 
	 * ************************************************************************
	 * format:
	 * Chr	gene_name	geneStart	geneEnd	exon_name	exonStart	exonEnd
	 * chr1	TNN			175077418	175147070	exon0	175077418	175077826
	 * chr1	TNN			175077418	175147070	exon1	175079332	175079706
	 * chr1	TNN			175077418	175147070	exon2	175080162	175080425
	 * chr1	TNN			175077418	175147070	exon3	175083749	175083934
	 * chr1	TNN			175077418	175147070	exon4	175085404	175085493
	 * chr1	TNN			175077418	175147070	exon5	175093989	175094252
	 * ************************************************************************
	 * 
	 */
	
	
	
	
	/**********
	 * Read-in exon frame data from TNN_exons.txt document;
	 * create N Exon_frame objects according to the lines in the TNN_exons.txt document;
	 * store all these Exon_frame objects to an ArrayList;
	 * 
	 */
	String file_name = "TNN";
	// ArrayList<Exon_frame> exon_List = Read_Exons_from_TXT(file_name);
	
	private static ArrayList<Exon_frame> Read_Exons_from_TXT(String gene_name) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		//create an arrayList to store all exons;
		ArrayList<Exon_frame> exon_List = new ArrayList<Exon_frame>();
		
		
		String routine = "D:/PhD/";
		String file_name = gene_name + "_exons.txt";
		
		Scanner read_exons = new Scanner(new File(routine + file_name));
		
		String title_line = read_exons.nextLine();
		System.out.println("For gene " + gene_name +" \n" + title_line );
		
		while(read_exons.hasNextLine()){
			
			//read in one exon, split by '\t'
			String currLine = read_exons.nextLine();
			String[] exon_info = currLine.split("\t");
			
			//create an object of Exon_frame
			Exon_frame currExon = new Exon_frame();
			
			//assign chr, gene_name, gene-start, gene-end, exon name, exon-start, and exon-end to currExon object
			currExon.Chr = exon_info[0];
			currExon.gene_name = exon_info[1];
			currExon.gene_start = Integer.parseInt( exon_info[2] );
			currExon.gene_end = Integer.parseInt( exon_info[3] );
			currExon.exon_name = exon_info[4];
			currExon.exon_start = Integer.parseInt( exon_info[5] );
			currExon.exon_end = Integer.parseInt( exon_info[6] );
			
			
			//add currExon object to the exons ArrayList;
			exon_List.add(currExon);
			
		}
		
		//close the scanner
		read_exons.close();
		
		return exon_List;
		
	}//end Read_exons_from_TXT() method;
	
	
	
	/***************
	 * 2nd
	 * From ExAC dataset, get gene variants for each position,
	 * save them to a txt document with the same name as the gene name plus variants;
	 * 
	 *  i.e. For all genes, we will name the variants txt document ExAC_variants.txt
	 *  ***************************************************************
	 *  format:
	 *  CHROM	Position	AC	PolyPhen
	 *  1		934388		5	N/A
	 *  1		934388		5	N/A
	 *  1		934444		1	benign
	 *  1		934444		1	N/A
	 *  1		934444		1	N/A
	 *  1		934447		1	benign
	 *  1		934447		1	N/A
	 *  ****************************************************************  
	 *  
	 *  Data from ExAc are considered as EXPECTED data in our model;
	 *  
	 */
	
	
	
	
	
	
	
	/******************
	 * 3rd
	 * Get target genes names from ALS dataset;
	 * 
	 * Store all genens names to an ArrayList; 
	 * for each gene, get the frame (regions) of each Exon from the CCDS dataset
	 * get all qualify variants within that gene&exon regions
	 * put all this variants into an arrayList
	 * 
	 * 
	 * 
	 * Data from ALS are considered as OBSERVED data in our model;
	 * @throws FileNotFoundException 
	 * 
	 */
	
	
	
	
	
	
	
	
	/**********
	 * main() function
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException{
		
		
		//get exons for TNN gene from TNN_exons.txt document
		ArrayList<Exon_frame> tnn_exons = Read_Exons_from_TXT("TNN");
		
		//get all allele-count reads from ExAC data, 0418_ExAC.gene.chr1.table
		ArrayList<Allele_Reads> tnn_reads = Get_Allele_Reads(tnn_exons);
		
		
		
	}//end main();



	/************
	 * get all qualify allele counts for a certain gene
	 * 
	 * pass by the exon ArrayList of that certain gene
	 * check each allele read in the ExAC dataset (or subdataset for that specific chr)
	 * 
	 * 
	 * @param exon_List
	 * @return
	 */
	private static ArrayList<Allele_Reads> Get_Allele_Reads(ArrayList<Exon_frame> exon_List) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

}//end everything;
