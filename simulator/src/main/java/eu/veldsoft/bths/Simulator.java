package eu.veldsoft.bths;

import java.security.SecureRandom;

import com.sun.star.script.provider.XScriptContext;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.frame.XModel;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.container.XIndexAccess;
import com.sun.star.sheet.XSpreadsheet;

public class Simulator {
	private static final SecureRandom PRNG = new SecureRandom();

	private static int paytable[][] = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,},
		{0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,},
		{0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,},
		{0,0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,},
	};

	private static int lines[][] = {
		{1,1,1,1,1,},
		{0,0,0,0,0,},
		{2,2,2,2,2,},
		{0,1,2,1,0,},
		{2,1,0,1,2,},
		{0,0,1,2,2,},
		{2,2,1,0,0,},
		{1,0,1,2,1,},
		{1,2,1,0,1,},
	};

	private static int baseGameReels[][] = {
		{1,3,4,5,6,7,8,9,10,11,12,},
		{1,3,4,5,6,7,8,9,10,11,12,15,16,},
		{1,3,4,5,6,7,8,9,10,11,12,15,16,},
		{1,3,4,5,6,7,8,9,10,11,12,15,16,},
		{1,3,4,5,6,7,8,9,10,11,12,},
	};

	private static int freeSpinsReels[][] = {
		{1,3,4,5,6,7,8,9,10,11,12,},
		{1,3,4,5,6,7,8,9,10,11,12,16,},
		{1,3,4,5,6,7,8,9,10,11,12,16,},
		{1,3,4,5,6,7,8,9,10,11,12,16,},
		{1,3,4,5,6,7,8,9,10,11,12,},
	};

	private static int singleBet = 1;
	private static int totalBet = 20;
	private static long wonMoney = 0L;
	private static long lostMoney = 0L;
	private static long totalNumberOfGames = 0L;
	private static long totalNumberOfFreeSpins = 0L;
	private static long totalNumberOfFreeSpinsStarts = 0L;
	private static long totalNumberOfFreeSpinsRestarts = 0L;
	private static long baseGameMoney = 0L;
	private static long freeSpinsMoney = 0L;
	private static long bonusGameMoney = 0L;
	private static long maxWin = 0L;
	private static long baseGameMaxWin = 0L;
	private static long freeSpinsMaxWin = 0L;
	private static long bonusGameMaxWin = 0L;
	private static long baseGameHitFrequency = 0L;
	private static long freeSpinsHitFrequency = 0L;
	private static long bonuseGameHitFrequency = 0L;
	private static int freeSpinsAmount = 0;
	private static int freeSpinsMultiplier = 0;

	private static int payingScatters[] = {16};
	private static int freeSpinsTrigerScatters[] = {15};
	private static int rewardFreeSpins[] = {0,0,0,3,5,7};
	private static int freeSpinsMultipliers[] = {0,0,0,1,2,3};

	private static int view[][] = {
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
	};

	private static int bingo[][][] = {
		{
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
		},
		{
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
		},
		{
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
		},
		{
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
		},
		{
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
		},
		{
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
			{ 0, 0, 0 },
		},
	};

	private static long baseGameSymbolsMoney[][] = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
	};

	private static long baseGameSymbolsHitFrequency[][] = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
	};

	private static long freeSpinsSymbolsMoney[][] = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
	};

	private static long freeSpinsSymbolsHitFrequency[][] = {
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,},
	};

	private static void spin(int reels[][]) {
		for (int i = 0, up, middle, down; i < reels.length; i++) {
			up = PRNG.nextInt( reels[i].length );
			middle = up + 1;
			down = up + 2;

			middle = middle % reels[i].length;
			down = down % reels[i].length;

			view[i][0] = reels[i][up];
			view[i][1] = reels[i][middle];
			view[i][2] = reels[i][down];
		}
	}

	private static int[] rewardFreeSpins(int scatter) {
		int result[] = {0,0};

		int numberOfScatters = 0;
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				if (view[i][j] == scatter) {
					numberOfScatters++;
				}
			}
		}

		result[0] = rewardFreeSpins[numberOfScatters];
		result[1] = freeSpinsMultipliers[numberOfScatters];

		return result;
	}

	private static int[] wildLineWin(int line[], int wild) {
		if (line[0] != wild) {
			return new int[] {0,-1,0};
		}

		int number = 0;
		for (int i = 0; i < line.length; i++) {
			if (line[i] != wild) {
				break;
			}
			number++;
		}

		int result[] = {0,0,0};
		result[0] = paytable[number][wild];
		result[1] = wild;
		result[2] = number;
		return result;
	}

	private static int scatterWin(int scatter) {
		int numberOfScatters = 0;
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				if (view[i][j] == scatter) {
					numberOfScatters++;
				}
			}
		}

		int win = paytable[numberOfScatters][scatter];

		if (win > 0 && freeSpinsAmount == 0) {
			baseGameSymbolsMoney[numberOfScatters][scatter] += win;
			baseGameSymbolsHitFrequency[numberOfScatters][scatter]++;
		} else if (win > 0 && freeSpinsAmount > 0) {
			freeSpinsSymbolsMoney[numberOfScatters][scatter] += win;
			freeSpinsSymbolsHitFrequency[numberOfScatters][scatter]++;
		}

		return win;
	}

	public static void simulate(XScriptContext ctx) {
		try {
			XModel model = ctx.getDocument();
			XSpreadsheetDocument document = UnoRuntime.queryInterface(XSpreadsheetDocument.class, model);
			XIndexAccess sheets = UnoRuntime.queryInterface(XIndexAccess.class, document.getSheets());
			XSpreadsheet first = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByIndex(0));

			first.getCellByPosition(0, 0).setFormula("Simulation!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}