package eu.veldsoft.bths;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
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
	private static Set<Integer> wilds = Set.of( 1 );
	private static Set<Integer> payingScatters = Set.of( 16 );
	private static Set<Integer> freeSpinsTrigerScatters = Set.of( 15 );
	private static int rewardFreeSpins[] = {0,0,0,3,5,7};
	private static int freeSpinsMultipliers[] = {0,0,0,1,2,3};

	private static int view[][] = {
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
		{ -1, -1, -1 },
	};

	private static int bingoCards[][] = {
		{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 0, 0, 0, 0, 0, 0, 0 },
	};

	private static boolean bingoNumbersOut[][] = {
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false,	false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,	false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false,	false, false, false, false },
		{ false, false, false, false, false, false, false, false, false, false, false,	false, false, false, false, false, false, false },
	};

	private static int numbersInRow[] = {};

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

	private static boolean fixRows() {
		boolean wasItChanged = false;

		boolean done = false;
		do {
			done = false;

			int a = -1;
			int b = -1;

			for (int j = 0; j < numbersInRow.length; j++) {
				if (numbersInRow[j] < 5) {
					a = j;
				}
				if (numbersInRow[j] > 5) {
					b = j;
				}
			}
			if (a == -1 || b == -1) {
				done = true;
				break;
			}

			int x = -1;
			for (int i = 0; i < 9; i++) {
				if (bingoCards[i][a] == 0 && bingoCards[i][b] != 0) {
					x = i;
					break;
				}
			}

			if (x == -1) {
				done = false;
				continue;
			}

			int swap = bingoCards[x][a];
			bingoCards[x][a] = bingoCards[x][b];
			bingoCards[x][b] = swap;
			numbersInRow[a]++;
			numbersInRow[b]--;
			wasItChanged = true;
		} while (done == false);

		return (wasItChanged);
	}

	private static void shuffleBingoCards() {
		int length = 0;
		for (int i = 0; i < bingoCards.length; i++) {
			for (int last = bingoCards[i].length - 1, r = -1, swap = -1; last > 0; last--) {
				r = PRNG.nextInt(last + 1);
				swap = bingoCards[i][last];
				bingoCards[i][last] = bingoCards[i][r];
				bingoCards[i][r] = swap;
			}

			if(bingoCards[i].length > length) {
				length = bingoCards[i].length;
			}
		}

		numbersInRow = new int[length];
		for (int j = 0; j < length; j++) {
			numbersInRow[j] = 0;
		}

		for (int j = 0; j < numbersInRow.length; j++) {
			for (int i = 0; i < bingoCards.length; i++) {
				numbersInRow[j] += (bingoCards[i][j] != 0 ? 1 : 0);
			}
		}
	}

	private static boolean fixThreeRows() {
		boolean wasItChanged = false;

		for (int i = 0; i < bingoCards.length; i++) {
			int a = -1;
			int b = -1;

			for (int j = 0; j < numbersInRow.length; j += 3) {
				if (0== (bingoCards[i][j + 0] != 0 ? 1 : 0)
				        + (bingoCards[i][j + 1] != 0 ? 1 : 0)
				        + (bingoCards[i][j + 2] != 0 ? 1 : 0)) {
					a = j + PRNG.nextInt(3);
				}
				if (3
				        == (bingoCards[i][j + 0] != 0 ? 1 : 0)
				        + (bingoCards[i][j + 1] != 0 ? 1 : 0)
				        + (bingoCards[i][j + 2] != 0 ? 1 : 0)) {
					b = j + PRNG.nextInt(3);
				}
			}

			if (a == -1 && b == -1) {
				continue;
			}
			if (a == -1) {
				do {
					a = PRNG.nextInt(numbersInRow.length);
				} while (bingoCards[i][a] != 0);
			}
			if (b == -1) {
				do {
					b = PRNG.nextInt(numbersInRow.length);
				} while (bingoCards[i][b] == 0);
			}

			int swap = bingoCards[i][a];
			bingoCards[i][a] = bingoCards[i][b];
			bingoCards[i][b] = swap;
			numbersInRow[a]++;
			numbersInRow[b]--;
			wasItChanged = true;
		}

		return (wasItChanged);
	}

	private static void resetBingoCards() {
		final int NUMBER_OF_SHAKES = 10 + PRNG.nextInt(11);

		int shakes = 0;
		boolean goOn = false;
		do {
			if (shakes <= 0) {
				shuffleBingoCards();
				shakes = NUMBER_OF_SHAKES;
			}

			goOn = fixRows();
			goOn = fixThreeRows() || goOn;
			shakes--;
		} while (goOn == true);

		for (int i = 0; i < bingoNumbersOut.length; i++) {
			for (int j = 0; j < bingoNumbersOut[i].length; j++) {
				bingoNumbersOut[i][j] = false;
			}
		}
	}

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

	private static int[] wildLineWin(int line[], int wild, int multiplier) {
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
		result[0] = paytable[number][wild] * multiplier;
		result[1] = wild;
		result[2] = number;
		return result;
	}

	private static int scatterWin(int scatter, int multiplier) {
		int numberOfScatters = 0;
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				if (view[i][j] == scatter) {
					numberOfScatters++;
				}
			}
		}

		int win = paytable[numberOfScatters][scatter] * multiplier;

		if (win > 0 && freeSpinsAmount == 0) {
			baseGameSymbolsMoney[numberOfScatters][scatter] += win;
			baseGameSymbolsHitFrequency[numberOfScatters][scatter]++;
		} else if (win > 0 && freeSpinsAmount > 0) {
			freeSpinsSymbolsMoney[numberOfScatters][scatter] += win;
			freeSpinsSymbolsHitFrequency[numberOfScatters][scatter]++;
		}

		return win;
	}

	private static int[] lineWin(int line[], int multiplier) {
		int symbol = line[0];
		for (int i = 0; i < line.length; i++) {
			if ( wilds.contains(line[i]) ) {
				symbol = line[i];
				break;
			}
		}

		for (int i = 0; i < line.length; i++) {
			if (wilds.contains(line[i])) {
				line[i] = symbol;
			} else {
				break;
			}
		}

		int number = 0;
		for (int i = 0; i < line.length; i++) {
			if (line[i] == symbol) {
				number++;
			} else {
				break;
			}
		}

		int result[] = {0, -1, 0};
		result[0] = paytable[number][symbol] * multiplier;
		result[1] = symbol;
		result[2] = number;

		return result;
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