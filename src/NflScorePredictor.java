import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NflScorePredictor {
	
	public static void main(String args[]) {
		
		int[][] nflScores = GetSampleNflScores();
			
		float[][] probablities = GetFixedProbability();
		//float[][] probablities = GetProbabilities(nflScores);

		Map<String, ArrayList<Float>> winningProbability = new HashMap<String, ArrayList<Float>>();
		Map<String, ArrayList<int[]>> boardAssignments = GetBoardAssignments();
		
		for (int i=0; i<4; i++)
		{
//			System.out.println(String.format("\nQuater [%s] Predictions\n", i+1));
//			float sum = 0;
//			for (int j=0; j<10; j++)
//			{
//				for (int k=0; k<10; k++)
//				{
//					sum += probablities[j][i] * probablities[k][i] * 100;
//					System.out.print(String.format("%.2f  ", probablities[j][i] * probablities[k][i] * 100));					
//				}
//				System.out.println();
//			}
//			System.out.println(sum);
			
			for (Map.Entry<String, ArrayList<int[]>> entry : boardAssignments.entrySet())
			{
				if (!winningProbability.containsKey(entry.getKey()))
				{
					winningProbability.put(entry.getKey(), new ArrayList<Float>());
				}
				
				float probability = 0f;
				for (int[] boardPosition : entry.getValue())
				{
					//probability += (probablities[boardPosition[0]][i] * probablities[boardPosition[1]][i]);
					probability += probablities[boardPosition[0]][boardPosition[1]];
				}
				
				winningProbability.get(entry.getKey()).add(probability);
			}
		}
		
		System.out.println("Probability of winning");
		
		float totalProbability = 0;
		for (Map.Entry<String, ArrayList<Float>> entry : winningProbability.entrySet())
		{
			System.out.println(String.format("For %s Quaters[1-4] %.2f - %.2f - %.2f - %.2f", entry.getKey(), 
					entry.getValue().get(0) * 100, entry.getValue().get(1) * 100, entry.getValue().get(2) * 100, entry.getValue().get(3) * 100));

			System.out.println(String.format("For %s atleast one quater %.2f", entry.getKey(), 
					(1 - ((1 - entry.getValue().get(0)) * (1 - entry.getValue().get(1)) * (1 - entry.getValue().get(2)) * (1 - entry.getValue().get(3)))) * 100));
			
			totalProbability += (1 - ((1 - entry.getValue().get(0)) * (1 - entry.getValue().get(1)) * (1 - entry.getValue().get(2)) * (1 - entry.getValue().get(3)))) * 100;
		}
		
		System.out.println(totalProbability);
	}
	
	private static float[][] GetFixedProbability() {
		float[][] fixedProbability = new float[][] {
			{0.0772f, 0.0215f, 0.008f,  0.1019f, 0.0589f, 0.0052f, 0.0372f, 0.1316f, 0.0117f, 0.01f},
			{0.0215f, 0.0046f, 0.0026f, 0.0167f, 0.0227f, 0.0033f, 0.0093f, 0.0252f, 0.0068f, 0.0048f},
			{0.008f,  0.0026f, 0.0002f, 0.0028f, 0.0061f, 0.0014f, 0.002f,  0.0078f, 0.0013f, 0.002f},
			{0.1019f, 0.0167f, 0.0028f, 0.0283f, 0.034f,  0.0037f, 0.0226f, 0.0773f, 0.0088f, 0.0087f},
			{0.0589f, 0.0227f, 0.0061f, 0.034f,  0.0165f, 0.0047f, 0.0148f, 0.0556f, 0.0089f, 0.0075f},
			{0.0052f, 0.0033f, 0.0014f, 0.0037f, 0.0047f, 0.0002f, 0.0015f, 0.0081f, 0.0029f, 0.0016f},
			{0.0372f, 0.0093f, 0.002f,  0.0226f, 0.0148f, 0.0015f, 0.054f,  0.0261f, 0.0041f, 0.0047f},
			{0.1316f, 0.0252f, 0.0078f, 0.0773f, 0.0556f, 0.0081f, 0.0261f, 0.0467f, 0.0098f, 0.0102f},
			{0.0117f, 0.0068f, 0.0013f,  0.0088f, 0.0089f, 0.0029f, 0.0041f, 0.0098f, 0.0015f, 0.0018f},
			{0.01f,   0.0048f, 0.002f,  0.0087f, 0.0075f, 0.0016f, 0.0047f, 0.0102f, 0.0018f, 0.0013f}
		};
		
		float sum=0;
		for (int i=0; i<10; i++)
		{
			for (int j=0; j<10; j++)
			{
				if (i != j)
				{
					fixedProbability[i][j] = fixedProbability[i][j]/2;
				}				
				sum += fixedProbability[i][j];
			}
		}
		
		System.out.println(sum);
		
		return fixedProbability;
	}

	private static float[][] GetProbabilities(int[][] nflScores) {
		int[][] numberOfOccurence = new int[10][4];
		int numOfScores = nflScores.length;
		
		for (int i=0; i<numOfScores; i++)
		{
			int score = nflScores[i][0];
			numberOfOccurence[score%10][0]++;
			
			score += nflScores[i][1];
			numberOfOccurence[score%10][1]++;
			
			score += nflScores[i][2];
			numberOfOccurence[score%10][2]++;
			
			score += nflScores[i][3];
			numberOfOccurence[score%10][3]++;
		}

		float[][] probablities = new float[10][4];

		for (int i=0; i<numberOfOccurence.length; i++)
		{
			probablities[i][0] = numberOfOccurence[i][0] * 1f/numOfScores;
			probablities[i][1] = numberOfOccurence[i][1] * 1f/numOfScores;
			probablities[i][2] = numberOfOccurence[i][2] * 1f/numOfScores;
			probablities[i][3] = numberOfOccurence[i][3] * 1f/numOfScores;
		}
		return probablities;
	}
	
	private static Map<String, ArrayList<int[]>> GetBoardAssignments()
	{
		Map<String,ArrayList<int[]>> boardAssignments = new HashMap<String, ArrayList<int[]>>();
		
		boardAssignments.put("BP", 
				new ArrayList<int[]>(Arrays.asList(new int[]{8, 1}, new int[]{6, 4}, new int[]{7, 5})));
		boardAssignments.put("Karthik", 
				new ArrayList<int[]>(Arrays.asList(new int[]{1, 1}, new int[]{1, 4}, new int[]{5, 7}, new int[]{7, 2}, new int[]{4, 0}, new int[]{3, 3}, new int[]{2, 8}, new int[]{8, 6}, new int[]{6, 6}, new int[]{9, 5})));
		boardAssignments.put("BO", 
				new ArrayList<int[]>(Arrays.asList(new int[]{7, 1}, new int[]{8, 0}, new int[]{1, 8}, new int[]{0, 6}, new int[]{3, 5})));
		boardAssignments.put("Mouli", 
				new ArrayList<int[]>(Arrays.asList(new int[]{5, 1}, new int[]{0, 4}, new int[]{4, 7}, new int[]{2, 2}, new int[]{1, 0}, new int[]{9, 3}, new int[]{0, 8}, new int[]{3, 9}, new int[]{7, 6}, new int[]{8, 5})));
		boardAssignments.put("Jason", 
				new ArrayList<int[]>(Arrays.asList(new int[]{3, 1}, new int[]{5, 8}, new int[]{8, 9}, new int[]{4, 6}, new int[]{5, 5})));
		boardAssignments.put("Pat", 
				new ArrayList<int[]>(Arrays.asList(new int[]{6, 1}, new int[]{1, 7}, new int[]{0, 9}, new int[]{4, 9}, new int[]{5, 6})));
		boardAssignments.put("Yenna", 
				new ArrayList<int[]>(Arrays.asList(new int[]{0, 1}, new int[]{5, 0}, new int[]{8, 3})));
		boardAssignments.put("Joarder", 
				new ArrayList<int[]>(Arrays.asList(new int[]{2, 1})));
		boardAssignments.put("AL", 
				new ArrayList<int[]>(Arrays.asList(new int[]{9, 1}, new int[]{8, 7}, new int[]{6, 2}, new int[]{7, 8}, new int[]{4, 5})));
		boardAssignments.put("Manju", 
				new ArrayList<int[]>(Arrays.asList(new int[]{4, 1}, new int[]{2, 4}, new int[]{3, 7}, new int[]{8, 2}, new int[]{0, 0}, new int[]{5, 3}, new int[]{6, 8}, new int[]{1, 9}, new int[]{9, 6}, new int[]{0, 5})));
		boardAssignments.put("Samson", 
				new ArrayList<int[]>(Arrays.asList(new int[]{1, 4}, new int[]{7, 7}, new int[]{5, 2}, new int[]{3, 0}, new int[]{2, 3})));
		boardAssignments.put("Thavorn", 
				new ArrayList<int[]>(Arrays.asList(new int[]{7, 4}, new int[]{2, 7}, new int[]{9, 2}, new int[]{2, 9}, new int[]{3, 6})));
		boardAssignments.put("TN", 
				new ArrayList<int[]>(Arrays.asList(new int[]{5, 4}, new int[]{3, 4}, new int[]{0, 2}, new int[]{2, 0}, new int[]{0, 3})));
		boardAssignments.put("Snijeesh", 
				new ArrayList<int[]>(Arrays.asList(new int[]{9, 2}, new int[]{1, 3}, new int[]{6, 9}, new int[]{1, 5}, new int[]{2, 5})));
		boardAssignments.put("Kume", 
				new ArrayList<int[]>(Arrays.asList(new int[]{4, 4}, new int[]{6, 7}, new int[]{4, 8}, new int[]{9, 9}, new int[]{1, 6})));
		boardAssignments.put("ChrisL", 
				new ArrayList<int[]>(Arrays.asList(new int[]{0, 7}, new int[]{4, 3}, new int[]{8, 8}, new int[]{5, 9})));
		boardAssignments.put("DanielC", 
				new ArrayList<int[]>(Arrays.asList(new int[]{9, 7}, new int[]{1, 2}, new int[]{6, 0}, new int[]{3, 8}, new int[]{7, 9})));
		boardAssignments.put("Sri", 
				new ArrayList<int[]>(Arrays.asList(new int[]{3, 2}, new int[]{9, 0}, new int[]{7, 3}, new int[]{6, 3}, new int[]{6, 5})));
		boardAssignments.put("Laura", 
				new ArrayList<int[]>(Arrays.asList(new int[]{4, 2}, new int[]{9, 8})));
		boardAssignments.put("Jillian", 
				new ArrayList<int[]>(Arrays.asList(new int[]{7, 0}, new int[]{2, 6})));
		
		return boardAssignments;
	}

	private static int[][] GetSampleNflScores() {
		int[][] nflScores = new int[][]
			{
				{6,3,3,6},
				{7,10,0,3},
				{0,7,0,8},
				{17,7,10,15},
				{3,3,7,7},
				{7,7,7,6},
				{0,6,7,7},
				{7,0,3,10},
				{0,0,14,10},
				{14,17,0,0},
				{7,3,3,3},
				{6,3,3,11},
				{7,6,7,10},
				{0,0,0,0},
				{0,6,9,3},
				{0,0,0,16},
				{0,0,0,10},
				{3,0,6,0},
				{0,17,7,11},
				{5,6,7,0},
				{0,3,3,0},
				{3,17,0,10},
				{7,7,3,11},
				{3,6,0,3},
				{0,7,10,0},
				{7,9,3,3},
				{0,3,7,0},
				{3,7,0,10},
				{7,7,3,7},
				{10,10,7,3},
				{0,9,0,7},
				{0,7,14,3},
				{21,3,3,7},
				{0,14,0,9},
				{7,7,3,3},
				{3,14,0,0},
				{7,3,7,7},
				{0,0,10,10},
				{14,7,7,7},
				{3,17,7,3},
				{3,13,0,0},
				{0,10,3,3},
				{3,0,7,0},
				{0,24,7,7},
				{0,10,0,7},
				{14,0,9,0},
				{3,3,7,7},
				{7,0,7,13},
				{10,20,6,0},
				{0,6,0,0},
				{3,3,14,0},
				{3,0,0,10}
			};
		return nflScores;
	}

}
