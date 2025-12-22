package eu.veldsoft.bths;

import java.util.Set;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.security.SecureRandom;

import com.sun.star.text.XText;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import com.sun.star.table.CellContentType;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.table.XTableColumns;
import com.sun.star.container.XIndexAccess;
import com.sun.star.util.URL;
import com.sun.star.util.XNumberFormats;
import com.sun.star.util.XNumberFormatTypes;
import com.sun.star.util.XNumberFormatsSupplier;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.PropertyValue;
import com.sun.star.script.provider.XScriptContext;

/**
 * Simulator of the slot machine.
 */
public class Simulator {
	/** Pseudorandom number generator. */
	private static final SecureRandom PRNG = new SecureRandom();

	/** Paytable values as an array. */
	private static int paytable[][] = {};

	/** Symbols used in the game. */
	private static String symbols[] = {};

	/** Lines used in the game. */
	private static int lines[][] = {};

	/** Base game reels configuration. */
	private static int baseGameReels[][] = {};

	/** Free spins reels configuration. */
	private static int freeSpinsReels[][] = {};

	/** Free spins reward configuration. */
	private static int rewardFreeSpins[] = {};

	/** Free spins multipliers configuration. */
	private static int freeSpinsMultipliers[] = {};

	/** Single bet amount. */
	private static int singleBet = 0;

	/** Total bet amount. */
	private static int totalBet = 0;

	/** Total won money during the simulation. */
	private static long wonMoney = 0L;

	/** Total lost money during the simulation. */
	private static long lostMoney = 0L;

	/** Total number of games played during the simulation. */
	private static long totalNumberOfGames = 0L;

	/** Total number of free spins played during the simulation. */
	private static long totalNumberOfFreeSpins = 0L;

	/** Total number of free spins started during the simulation. */
	private static long totalNumberOfFreeSpinsStarts = 0L;

	/** Total number of free spins restarted during the simulation. */
	private static long totalNumberOfFreeSpinsRestarts = 0L;

	/** Total money won in during base game. */
	private static long baseGameMoney = 0L;

	/** Total money won in during free spins. */
	private static long freeSpinsMoney = 0L;

	/** Total money won in during bonus game. */
	private static long bonusGameMoney = 0L;

	/** Maximum win recorded for a single bet. */
	private static long maxWin = 0L;

	/** Maximum win recorded in base game. */
	private static long baseGameMaxWin = 0L;

	/** Maximum win recorded in free spins. */
	private static long freeSpinsMaxWin = 0L;

	/** Maximum win recorded in bonus game. */
	private static long bonusGameMaxWin = 0L;

	/** Base game hit frequency. */
	private static long baseGameHitFrequency = 0L;

	/** Free spins hit frequency. */
	private static long freeSpinsHitFrequency = 0L;

	/** Bonus game hit frequency. */
	private static long bonusGameHitFrequency = 0L;

	/** Bingo line money won. */
	private static long bingoLineMoney = 0L;

	/** Bingo full house money won. */
	private static long bingoFullHouseMoney = 0L;

	/** Bingo line hit frequency. */
	private static long bingoLineHitFrequency = 0L;

	/** Bingo full house hit frequency. */
	private static long bingoFullHouseHitFrequency = 0L;

	/** Statistics for wins by symbols in base game. */
	private static long baseGameSymbolsMoney[][] = {};

	/** Statistics for hits by symbols in base game. */
	private static long baseGameSymbolsHitFrequency[][] = {};

	/** Statistics for wins by symbols in free spins. */
	private static long freeSpinsSymbolsMoney[][] = {};

	/** Statistics for hits by symbols in free spins. */
	private static long freeSpinsSymbolsHitFrequency[][] = {};

	/** List of wild symbols in the game. */
	private static Set<Integer> wilds = new HashSet<>();

	/** List of paying scatter symbols in the game. */
	private static Set<Integer> payingScatters = new HashSet<>();

	/** List of free spins trigger scatter symbols in the game. */
	private static Set<Integer> freeSpinsTrigerScatters = new HashSet<>();

	/** List of bingo line symbols in the game. */
	private static Set<Integer> bingoLineSymbols = new HashSet<>();

	/** List of bingo full house symbols in the game. */
	private static Set<Integer> bingoFullHouseSymbols = new HashSet<>();

	/** Helping array for the game screen configuration. */
	private static int view[][] = {};

	/** Bingo cards for the bonus game. */
	private static int bingoCards[][] = {
		{  1,  2,  3,  4,  5,  6,  7,  8,  9,  0,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 40, 41, 42, 43, 44, 45, 46, 47, 48, 49,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 60, 61, 62, 63, 64, 65, 66, 67, 68, 69,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,  0, 0, 0, 0, 0, 0, 0, 0 },
		{ 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 0, 0, 0, 0, 0, 0, 0 },
	};

	/** Numbers out in the bingo game (only flags). */
	private static boolean bingoNumbersOut[][] = {
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false,	false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false,	false, false, false, false },
		{ false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false },
	};

	/** Helper array for bingo cards handling. */
	private static int numbersInRow[] = {};

	/**
	 * Fix all rows to have only 5 numbers.
	 *
	 * @return True if fix was done, false otherwise.
	 */
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

		return wasItChanged;
	}

	/**
	 * Shuffle the numbers in single bingo card.
	 */
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

	/**
	 * Fix all rows to have only 5 numbers.
	 *
	 * @return True if fix was done, false otherwise.
	 */
	private static boolean fixThreeRows() {
		boolean wasItChanged = false;

		for (int i = 0; i < bingoCards.length; i++) {
			int a = -1;
			int b = -1;

			for (int j = 0; j < numbersInRow.length; j += 3) {
				if (0 == (bingoCards[i][j + 0] != 0 ? 1 : 0)
				        + (bingoCards[i][j + 1] != 0 ? 1 : 0)
				        + (bingoCards[i][j + 2] != 0 ? 1 : 0)) {
					a = j + PRNG.nextInt(3);
				}
				if (3 == (bingoCards[i][j + 0] != 0 ? 1 : 0)
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

		return wasItChanged;
	}

	/**
	 * Reset bingo cards for a new game.
	 */
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

	/** Index of the bingo line in the bingo card. */
	private static int bingoLineIndex = -1;

	/** Index of the card with the bingo in it. */
	private static int bingoCardIndex = -1;

	/**
	 * Generate random bingo card with 6 talons in it. Also mark the card as empty.
	 */
	private static void generateRandomBingoCard() {
		final int NUMBER_OF_SHAKES = 30;

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
			for (int j = 0; j < bingoNumbersOut[0].length; j++) {
				bingoNumbersOut[i][j] = false;
			}
		}

		bingoLineIndex = -1;
		bingoCardIndex = -1;
	}

	/**
	 * Perform a spin on the given reels and update the view array.
	 *
	 * @param reels Reels to be spun.
	 */
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

	/**
	 * Calculate number of free spins and multiplier from scatter symbols.
	 *
	 * @param scatter Scatter symbol to be evaluated.
	 *
	 * @return Array with number of free spins and multiplier.
	 */
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

		if (result[0] > 0 && freeSpinsAmount <= 0) {
			baseGameSymbolsHitFrequency[scatter][numberOfScatters]++;
		} else if (result[0] > 0 && freeSpinsAmount > 0) {
			freeSpinsSymbolsHitFrequency[scatter][numberOfScatters]++;
		}

		return result;
	}

	/**
	 * Calculate win from a single line with wild symbols only.
	 *
	 * @param line Line to be evaluated.
	 * @param wild Wild symbol.
	 * @param multiplier Win multiplier.
	 *
	 * @return Array with win amount, symbol and number of symbols in the win.
	 */
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

	/**
	 * Calculate win from scatter symbols.
	 *
	 * @param scatter Scatter symbol to be evaluated.
	 * @param multiplier Win multiplier.
	 *
	 * @return Win from the scatter symbols.
	 */
	private static int scatterWin(int scatter, int multiplier) {
		int numberOfScatters = 0;
		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				if (view[i][j] == scatter) {
					numberOfScatters++;
				}
			}
		}

		int win = paytable[scatter][numberOfScatters] * multiplier;

		if (win > 0 && freeSpinsAmount == 0) {
			baseGameSymbolsMoney[scatter][numberOfScatters] += win;
			baseGameSymbolsHitFrequency[scatter][numberOfScatters]++;
		} else if (win > 0 && freeSpinsAmount > 0) {
			freeSpinsSymbolsMoney[scatter][numberOfScatters] += win;
			freeSpinsSymbolsHitFrequency[scatter][numberOfScatters]++;
		}

		return win;
	}

	/**
	 * Calculate win from a single line.
	 *
	 * @param line Line to be evaluated.
	 * @param multiplier Win multiplier.
	 *
	 * @return Array with win amount, symbol and number of symbols in the win.
	 */
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
		result[0] = paytable[symbol][number] * multiplier;
		result[1] = symbol;
		result[2] = number;

		return result;
	}

	/**
	 * Calculate total win from all lines in the current spin.
	 *
	 * @return Total win from all lines.
	 */
	private static int linesWin() {
		int currentWin = 0;

		for (int l = 0; l < lines.length; l++) {
			int line[] = new int[view.length];
			for (int c = 0; c < view.length; c++) {
				line[c] = view[c][ lines[l][c] ];
			}

			int result[] = lineWin(line, (freeSpinsAmount > 0 ? freeSpinsMultiplier : 1) );

			int win = result[0];
			int symbol = result[1];
			int number = result[2];

			if (win > 0) {
				currentWin += win;

				if (freeSpinsAmount == 0) {
					baseGameSymbolsMoney[symbol][number] += win;
					baseGameSymbolsHitFrequency[symbol][number]++;
				} else {
					freeSpinsSymbolsMoney[symbol][number] += win;
					freeSpinsSymbolsHitFrequency[symbol][number]++;
				}
			}
		}

		return currentWin;
	}

	/**
	 * Mark bingo number and return it to the caller.
	 *
	 * @param line Line with a win in current spin.
	 * @param symbol Symbol of the win.
	 *
	 * @return The number marked.
	 */
	private static int markBallOut(int line, int symbol) {
		boolean canBeFound = false;

		/* Check for available numbers. */
		for (int i = 0; i < bingoNumbersOut.length; i++) {
			for (int j = 0; j < bingoNumbersOut[i].length; j++) {
				if (bingoNumbersOut[i][j] == false && bingoCards[i][j] != 0) {
					canBeFound = true;
				}
			}
		}

		/* It should not be possible to search for numbers when there is no any. */
		if (canBeFound == false) {
			return -1;
		}

		int i = -1;
		int j = -1;
		do {
			i = PRNG.nextInt( bingoNumbersOut.length );
			j = PRNG.nextInt( bingoNumbersOut[i].length );
		} while (bingoNumbersOut[i][j] == true || bingoCards[i][j] == 0);

		bingoNumbersOut[i][j] = true;

		return bingoCards[i][j];
	}

	/**
	 * Check is there a bingo line combination.
	 *
	 * @return True if there is a bingo line, false otherwise.
	 */
	private static boolean checkForBingoLine() {
		if (bingoLineIndex != -1) {
			return (false);
		}

		for (int j = 0; j < bingoNumbersOut[0].length; j++) {
			int count = 0;
			for (int i = 0; i < bingoNumbersOut.length; i++) {
				if (bingoNumbersOut[i][j] == true && bingoCards[i][j] != 0) {
					count++;
				}
			}

			if (count > 5) {
				/* It should not be possible. */
			} else if (count == 5) {
				bingoLineIndex = j;
				return (true);
			}
		}

		return (false);
	}
	/**
	 * Check is there a bingo combination.
	 *
	 * @return True if there is a bingo, false otherwise.
	 */
	private static boolean checkForFullHouse() {
		if (bingoCardIndex != -1) {
			return (false);
		}

		int count = 0;
		for (int j = 0; j < bingoNumbersOut[0].length; j++) {
			if (j % 3 == 0) {
				count = 0;
			}

			for (int i = 0; i < bingoNumbersOut.length; i++) {
				if (bingoNumbersOut[i][j] == true && bingoCards[i][j] != 0) {
					count++;
				}
			}

			if (count > 15) {
				/* It should not be possible. */
			} else if (count == 15) {
				bingoCardIndex = j / 3;
				return (true);
			}
		}

		return (false);
	}

	/** Helping variable for the free spins mode as number of spins. */
	private static int freeSpinsAmount = 0;

	/** Helping variable in free spins mode for the active win multiplier. */
	private static int freeSpinsMultiplier = 0;

	/**
	 * Setup free spins parameters.
	 */
	private static void setupFreeSpins() {
		boolean isInitialStart = (freeSpinsAmount <= 0);

		if (freeSpinsAmount <= 0) {
			freeSpinsAmount = 0;
			freeSpinsMultiplier = 0;
		}

		for(int scatter: freeSpinsTrigerScatters) {
			int parameters[] = rewardFreeSpins(scatter);

			if(parameters[0] <= 0) {
				continue;
			}

			if(isInitialStart == true) {
				totalNumberOfFreeSpinsStarts++;
			} else {
				totalNumberOfFreeSpinsRestarts++;
			}

			freeSpinsAmount += parameters[0];
			freeSpinsMultiplier = Math.max(freeSpinsMultiplier, parameters[1]);
		}
	}

	/**
	 * Perform a single base game spin.
	 */
	private static void singleBaseGame() {
		spin(baseGameReels);

		/* Setup free spins parameters. */
		setupFreeSpins();

		markBallOut(-1, -1);

		int win3 = 0;
		if (checkForBingoLine() == true) {
			win3 = paytable[bingoLineSymbols.iterator().next()][1];
			bonusGameMoney += win3;
			bingoLineMoney += win3;
			bingoLineHitFrequency++;
			if(bonusGameMaxWin < win3) {
				bonusGameMaxWin = win3;
			}
		}

		int win4 = 0;
		if (checkForFullHouse() == true) {
			win4 = paytable[bingoFullHouseSymbols.iterator().next()][1];
			bonusGameMoney += win4;
			bingoFullHouseMoney += win4;
			bingoFullHouseHitFrequency++;

			if(bonusGameMaxWin < win4) {
				bonusGameMaxWin = win4;
			}

			/* Reset to a new bingo game. */
			generateRandomBingoCard();
		}

		if(win3+win4 > 0) {
			bonusGameHitFrequency++;
		}

		int win2 = 0;
		for(int scatter : payingScatters) {
			win2 += scatterWin(scatter, 1);
		}
		if(win2 > 0) {
			baseGameMoney += win2;
		}

		int win1 = linesWin();
		if(win1 > 0) {
			baseGameMoney += win1;
		}

		int totalWin = win1 + win2 + win3 + win4;

		if(totalWin > 0) {
			baseGameHitFrequency++;
		}

		if(totalWin > baseGameMaxWin) {
			baseGameMaxWin = totalWin;
		}

		wonMoney += totalWin;
		totalNumberOfGames++;
	}

	/**
	 * Perform a single free spin.
	 */
	private static void singleFreeSpin() {
		spin(freeSpinsReels);

		int win2 = 0;
		for(int scatter : payingScatters) {
			win2 += scatterWin(scatter, freeSpinsMultiplier);
		}
		if(win2 > 0) {
			freeSpinsMoney += win2;
		}

		int win1 = linesWin();
		if(win1 > 0) {
			freeSpinsMoney += win1;
		}

		int totalWin = win1 + win2;

		if(totalWin > 0) {
			freeSpinsHitFrequency++;
		}

		if(totalWin > freeSpinsMaxWin) {
			freeSpinsMaxWin = totalWin;
		}

		wonMoney += totalWin;
		totalNumberOfFreeSpins++;
	}

	/** How many Monte Carlo cycles to be executed. */
	private static long numberOfSimulations = 0L;

	/**
	 * Read data structures from the spreadsheets.
	 *
	 * @param ctx Script context.
	 */
	private static void readDataStructures(XScriptContext ctx) {
		try {
			XModel model = ctx.getDocument();
			XSpreadsheetDocument document = UnoRuntime.queryInterface(XSpreadsheetDocument.class, model);
			XSpreadsheets sheets = UnoRuntime.queryInterface(XSpreadsheets.class, document.getSheets());

			/* Reed game parameters. */
			XSpreadsheet summary = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByName("Summary"));
			int numberOfRows = (int)summary.getCellByPosition(1,1).getValue();
			int numberOfColumns = (int)summary.getCellByPosition(1,2).getValue();
			int numberOfBettingLines = (int)summary.getCellByPosition(1,3).getValue();
			numberOfSimulations = (long)summary.getCellByPosition(1,10).getValue();

			view = new int[numberOfColumns][numberOfRows];

			/* Read paytable. */
			XSpreadsheet paytable = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByName("Paytable"));
			int tableRows = 0;
			int tableColumns = 0;
			for (int c = 4; paytable.getCellByPosition(c,1).getType()!=CellContentType.EMPTY; c++) {
				tableColumns++;
			}
			for (int r = 1; paytable.getCellByPosition(4, r).getType()!=CellContentType.EMPTY; r++) {
				tableRows++;
			}
			Simulator.paytable = new int[tableRows][tableColumns];
			for (int c = 0; c<tableColumns; c++) {
				for (int r = 0; r<tableRows; r++) {
					Simulator.paytable[r][c] = (int)paytable.getCellByPosition(4+c,1+r).getValue();
				}
			}
			wilds = new HashSet<>();
			payingScatters = new HashSet<>();
			freeSpinsTrigerScatters = new HashSet<>();
			bingoLineSymbols = new HashSet<>();
			bingoFullHouseSymbols = new HashSet<>();
			for (int r = 0; r<tableRows; r++) {
				if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(2,1+r)).getString().equals("wild")) {
					wilds.add( (int)paytable.getCellByPosition(3,1+r).getValue() );
				}
				if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(2,1+r)).getString().equals("scatter")) {
					payingScatters.add( (int)paytable.getCellByPosition(3,1+r).getValue() );
				}
				if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(2,1+r)).getString().equals("free")) {
					freeSpinsTrigerScatters.add( (int)paytable.getCellByPosition(3,1+r).getValue() );
				}
				if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(2,1+r)).getString().equals("bonus")) {
					if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(1,1+r)).getString().equals("Line Bonus")) {
						bingoLineSymbols.add( (int)paytable.getCellByPosition(3,1+r).getValue() );
					}
				}
				if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(2,1+r)).getString().equals("bonus")) {
					if(UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(1,1+r)).getString().equals("Bingo Bonus")) {
						bingoFullHouseSymbols.add( (int)paytable.getCellByPosition(3,1+r).getValue() );
					}
				}
			}
			symbols = new String[tableRows];
			for (int r = 0; r<tableRows; r++) {
				symbols[r] = UnoRuntime.queryInterface(XText.class, paytable.getCellByPosition(1,1+r)).getString();
			}

			/* Read lines. */
			XSpreadsheet lines = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByName("Lines"));
			Simulator.lines = new int[numberOfBettingLines][numberOfColumns];
			for (int c = 0; c<numberOfColumns; c++) {
				for (int l = 0; l<numberOfBettingLines; l++) {
					Simulator.lines[l][c] = (int)lines.getCellByPosition(2+c,1+l).getValue();
				}
			}

			/* Reed base game reels. */
			XSpreadsheet baseGameReels = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByName("Base Reels"));
			Simulator.baseGameReels = new int[numberOfColumns][];
			for (int c = 0; c<numberOfColumns; c++) {
				int length = 0;
				for (int r = 1; baseGameReels.getCellByPosition(c,r).getType()!=CellContentType.EMPTY; r++) {
					length++;
				}
				Simulator.baseGameReels[c] = new int[length];
				for (int r = 0; r<length; r++) {
					Simulator.baseGameReels[c][r] = (int)baseGameReels.getCellByPosition(c,1+r).getValue();
				}
			}

			/* Reed free spins reels. */
			XSpreadsheet freeSpinsReels = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByName("Free Reels"));
			Simulator.freeSpinsReels = new int[numberOfColumns][];
			for (int c = 0; c<numberOfColumns; c++) {
				int length = 0;
				for (int r = 1; freeSpinsReels.getCellByPosition(c,r).getType()!=CellContentType.EMPTY; r++) {
					length++;
				}
				Simulator.freeSpinsReels[c] = new int[length];
				for (int r = 0; r<length; r++) {
					Simulator.freeSpinsReels[c][r] = (int)freeSpinsReels.getCellByPosition(c,1+r).getValue();
				}
			}

			/* Reed free spins parameters. */
			XSpreadsheet freeSpinsParameters = UnoRuntime.queryInterface(XSpreadsheet.class, sheets.getByName("Free Spins"));
			{
				List<Integer> values = new ArrayList<>();
				for (int c = 1; freeSpinsParameters.getCellByPosition(c,1).getType()!=CellContentType.EMPTY; c++) {
					values.add((int)freeSpinsParameters.getCellByPosition(c,1).getValue());
				}
				Simulator.rewardFreeSpins = new int[values.size()];
				for (int i = 0; i < values.size(); i++) {
					Simulator.rewardFreeSpins[i] = values.get(i);
				}
			}
			{
				List<Integer> values = new ArrayList<>();
				for (int c = 1; freeSpinsParameters.getCellByPosition(c,2).getType()!=CellContentType.EMPTY; c++) {
					values.add((int)freeSpinsParameters.getCellByPosition(c,2).getValue());
				}
				Simulator.freeSpinsMultipliers = new int[values.size()];
				for (int i = 0; i < values.size(); i++) {
					Simulator.freeSpinsMultipliers[i] = values.get(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reset all statistics variables.
	 */
	private static void resetStatistics() {
		singleBet = 1;

		totalBet = lines.length * singleBet;

		wonMoney = 0L;
		lostMoney = 0L;

		totalNumberOfGames = 0L;
		totalNumberOfFreeSpins = 0L;
		totalNumberOfFreeSpinsStarts = 0L;
		totalNumberOfFreeSpinsRestarts = 0L;

		baseGameMoney = 0L;
		freeSpinsMoney = 0L;
		bingoLineMoney = 0L;
		bingoLineHitFrequency = 0L;
		bingoFullHouseMoney = 0L;
		bingoFullHouseHitFrequency = 0L;
		bonusGameMoney = 0L;

		maxWin = 0L;
		baseGameMaxWin = 0L;
		freeSpinsMaxWin = 0L;
		bonusGameMaxWin = 0L;
		baseGameHitFrequency = 0L;
		freeSpinsHitFrequency = 0L;
		bonusGameHitFrequency = 0L;

		baseGameSymbolsMoney = new long[paytable.length][];
		for(int i = 0; i < paytable.length; i++) {
			baseGameSymbolsMoney[i] = new long[paytable[i].length];
			for(int j = 0; j < paytable[i].length; j++) {
				baseGameSymbolsMoney[i][j] = 0L;
			}
		}

		baseGameSymbolsHitFrequency = new long[paytable.length][];
		for(int i = 0; i < paytable.length; i++) {
			baseGameSymbolsHitFrequency[i] = new long[paytable[i].length];
			for(int j = 0; j < paytable[i].length; j++) {
				baseGameSymbolsHitFrequency[i][j] = 0L;
			}
		}

		freeSpinsSymbolsMoney = new long[paytable.length][];
		for(int i = 0; i < paytable.length; i++) {
			freeSpinsSymbolsMoney[i] = new long[paytable[i].length];
			for(int j = 0; j < paytable[i].length; j++) {
				freeSpinsSymbolsMoney[i][j] = 0L;
			}
		}

		freeSpinsSymbolsHitFrequency = new long[paytable.length][];
		for(int i = 0; i < paytable.length; i++) {
			freeSpinsSymbolsHitFrequency[i] = new long[paytable[i].length];
			for(int j = 0; j < paytable[i].length; j++) {
				freeSpinsSymbolsHitFrequency[i][j] = 0L;
			}
		}

		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				view[i][j] =-1;
			}
		}
	}

	/**
	 * Auto size all columns in the given sheet.
	 *
	 * @param sheet Sheet to be processed.
	 * @param upTo Number of columns to be auto sized.
	 *
	 * @throws Exception If auto sizing fails.
	 */
	private static void autoSizeColums(XSpreadsheet sheet, int upTo) throws Exception {
		XTableColumns columns = (UnoRuntime.queryInterface(XColumnRowRange.class, sheet)).getColumns();
		for (int i = 0; i <= upTo; i++) {
			XPropertySet property = UnoRuntime.queryInterface(XPropertySet.class, columns.getByIndex(i));
			property.setPropertyValue("OptimalWidth", Boolean.TRUE);
		}
	}

	/**
	 * Set percent format to the given cell.
	 *
	 * @param document Spreadsheet document.
	 * @param sheet Sheet where the cell is located.
	 * @param column Column of the cell.
	 * @param row Row of the cell.
	 *
	 * @throws Exception If setting the format fails.
	 */
	private static void setPercent(XSpreadsheetDocument document, com.sun.star.sheet.XSpreadsheet sheet, int column, int row) throws Exception {
		XNumberFormatsSupplier supplier = UnoRuntime.queryInterface(XNumberFormatsSupplier.class, document);
		XNumberFormats formats = supplier.getNumberFormats();

		int format = formats.queryKey("0.00%", new com.sun.star.lang.Locale("en", "US", ""), false);
		if (format == -1) {
			format = formats.addNew("0.00%", new com.sun.star.lang.Locale("en", "US", ""));
		}

		XCell cell = UnoRuntime.queryInterface(XCell.class,sheet.getCellByPosition(column, row));
		XPropertySet set = UnoRuntime.queryInterface(XPropertySet.class, cell);
		set.setPropertyValue("NumberFormat", format);
	}

	/**
	 * Set digits after the deciaml point format to the given cell.
	 *
	 * @param document Spreadsheet document.
	 * @param sheet Sheet where the cell is located.
	 * @param column Column of the cell.
	 * @param row Row of the cell.
	 *
	 * @throws Exception If setting the format fails.
	 */
	private static void setDigits(XSpreadsheetDocument document, com.sun.star.sheet.XSpreadsheet sheet, int column, int row) throws Exception {
		XNumberFormatsSupplier supplier = UnoRuntime.queryInterface(XNumberFormatsSupplier.class, document);
		XNumberFormats formats = supplier.getNumberFormats();

		int format = formats.queryKey("0.00", new com.sun.star.lang.Locale("en", "US", ""), false);
		if (format == -1) {
			format = formats.addNew("0.00", new com.sun.star.lang.Locale("en", "US", ""));
		}

		XCell cell = UnoRuntime.queryInterface(XCell.class,sheet.getCellByPosition(column, row));
		XPropertySet set = UnoRuntime.queryInterface(XPropertySet.class, cell);
		set.setPropertyValue("NumberFormat", format);
	}

	/**
	 * Report simulation statistics into a new sheet.
	 *
	 * @param ctx Script context.
	 */
	private static void repoertStatistics(XScriptContext ctx) {
		try {
			XModel model = ctx.getDocument();
			XSpreadsheetDocument document = UnoRuntime.queryInterface(XSpreadsheetDocument.class, model);
			XSpreadsheets sheets = document.getSheets();

			/* Simulation report sheet. */
			short index = (short)sheets.getElementNames().length;
			String name = "Simulaton Report - " + (new Date()).toString().replace(":", " ");
			sheets.insertNewByName(name, index);

			XSpreadsheet report = UnoRuntime.queryInterface(XSpreadsheet.class,sheets.getByName(name));

			/* Sheet offset for rows. */
			int offset = 0;
			int resize = 0;

			report.getCellByPosition(0, offset).setFormula("Total Loss:");
			report.getCellByPosition(1, offset).setFormula("" + lostMoney);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Total Win:");
			report.getCellByPosition(1, offset).setFormula("" + wonMoney);
			offset += 1;

			/* Prevent division by zero exception. */
			lostMoney = (lostMoney == 0L ? 1L : lostMoney);

			report.getCellByPosition(0, offset).setFormula("Total RTP [%]:");
			report.getCellByPosition(1, offset).setFormula("" + (double)wonMoney / (double)lostMoney);
			setPercent(document, report, 1, offset);

			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Number of Games:");
			report.getCellByPosition(1, offset).setFormula("" + totalNumberOfGames);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Number of Free Spins:");
			report.getCellByPosition(1, offset).setFormula("" + totalNumberOfFreeSpins);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Number of Free Spins Starts:");
			report.getCellByPosition(1, offset).setFormula("" + totalNumberOfFreeSpinsStarts);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Number of Free Spins Restarts:");
			report.getCellByPosition(1, offset).setFormula("" + totalNumberOfFreeSpinsRestarts);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Base Game Win:");
			report.getCellByPosition(1, offset).setFormula("" + baseGameMoney);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Free Spins Win:");
			report.getCellByPosition(1, offset).setFormula("" + freeSpinsMoney);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bonus Game Win:");
			report.getCellByPosition(1, offset).setFormula("" + bonusGameMoney);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Base Game RTP [%]:");
			report.getCellByPosition(1, offset).setFormula("" + ( (double)baseGameMoney / (double)lostMoney ) );
			setPercent(document, report, 1, offset);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Free Spins RTP [%]:");
			report.getCellByPosition(1, offset).setFormula("" + ( (double)freeSpinsMoney / (double)lostMoney ) );
			setPercent(document, report, 1, offset);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bonus Game RTP [%]:");
			report.getCellByPosition(1, offset).setFormula("" + ( (double)bonusGameMoney * 100D / (double)lostMoney ) );
			setPercent(document, report, 1, offset);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Max Win:");
			report.getCellByPosition(1, offset).setFormula("" + maxWin);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Base Game Max Win:");
			report.getCellByPosition(1, offset).setFormula("" + baseGameMaxWin);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Free Spins Max Win:");
			report.getCellByPosition(1, offset).setFormula("" + freeSpinsMaxWin);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bonus Game Max Win:");
			report.getCellByPosition(1, offset).setFormula("" + bonusGameMaxWin);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Base Game Hit Frequency:");
			report.getCellByPosition(1, offset).setFormula("" + baseGameHitFrequency);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Free Spins Hit Frequency:");
			report.getCellByPosition(1, offset).setFormula("" + freeSpinsHitFrequency);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bonus Game Hit Frequency:");
			report.getCellByPosition(1, offset).setFormula("" + bonusGameHitFrequency);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Bingo Line Win:");
			report.getCellByPosition(1, offset).setFormula("" + bingoLineMoney);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bingo Full House Win:");
			report.getCellByPosition(1, offset).setFormula("" + bingoFullHouseMoney);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Bingo Line RTP [%]:");
			report.getCellByPosition(1, offset).setFormula("" + ( (double)bingoLineMoney / (double)lostMoney ) );
			setPercent(document, report, 1, offset);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bingo Full House RTP [%]:");
			report.getCellByPosition(1, offset).setFormula("" + ( (double)bingoFullHouseMoney / (double)lostMoney ) );
			setPercent(document, report, 1, offset);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Bingo Line Hit Frequency:");
			report.getCellByPosition(1, offset).setFormula("" + bingoLineHitFrequency);
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Bingo Full House Hit Frequency:");
			report.getCellByPosition(1, offset).setFormula("" + bingoFullHouseHitFrequency);
			offset += 2;

			report.getCellByPosition(0, offset).setFormula("Base Game Symbols Win:");
			offset += 1;
			for(int i = 0, l=baseGameSymbolsMoney[0].length; i < baseGameSymbolsMoney[0].length; i++) {
				report.getCellByPosition(1+i, offset).setFormula( "" + i + " of" );
				resize = (1+i > resize) ? 1+i : resize;
				report.getCellByPosition(2+l+i, offset).setFormula( "" + i + " of" );
				resize = (2+l+i > resize) ? 2+l+i : resize;
			}
			offset += 1;
			for(int j = 0; j < symbols.length; j++) {
				report.getCellByPosition(0, offset+j).setFormula( symbols[j] );
			}
			for(int j = 0; j < baseGameSymbolsMoney.length; j++) {
				for(int i = 0, l=baseGameSymbolsMoney[j].length; i < baseGameSymbolsMoney[j].length; i++) {
					report.getCellByPosition(1+i, offset+j).setFormula("" + baseGameSymbolsMoney[j][i]);
					resize = (1+i > resize) ? 1+i : resize;
					report.getCellByPosition(2+l+i, offset+j).setFormula("" + baseGameSymbolsMoney[j][i]/(double)lostMoney);
					setDigits(document, report, 2+l+i, offset+j);
					resize = (2+l+i > resize) ? 2+l+i : resize;
				}
			}
			offset += baseGameSymbolsMoney.length;
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Free Spins Symbols Win:");
			offset += 1;
			for(int i = 0, l=freeSpinsSymbolsMoney[0].length; i < freeSpinsSymbolsMoney[0].length; i++) {
				report.getCellByPosition(1+i, offset).setFormula("" + i + " of");
				resize = (1+i > resize) ? 1+i : resize;
				report.getCellByPosition(2+l+i, offset).setFormula( "" + i + " of" );
				resize = (2+l+i > resize) ? 2+l+i : resize;
			}
			offset += 1;
			for(int j = 0; j < symbols.length; j++) {
				report.getCellByPosition(0, offset+j).setFormula( symbols[j] );
			}
			for(int j = 0; j < freeSpinsSymbolsMoney.length; j++) {
				for(int i = 0, l=freeSpinsSymbolsMoney[j].length; i < freeSpinsSymbolsMoney[j].length; i++) {
					report.getCellByPosition(1+i, offset+j).setFormula("" + freeSpinsSymbolsMoney[j][i]);
					resize = (1+i > resize) ? 1+i : resize;
					report.getCellByPosition(2+l+i, offset+j).setFormula("" + freeSpinsSymbolsMoney[j][i]/(double)lostMoney);
					setDigits(document, report, 2+l+i, offset+j);
					resize = (2+l+i > resize) ? 2+l+i : resize;
				}
			}
			offset += freeSpinsSymbolsMoney.length;
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Base Game Symbols Frequencies:");
			offset += 1;
			for(int i = 0, l=baseGameSymbolsHitFrequency[0].length; i < baseGameSymbolsHitFrequency[0].length; i++) {
				report.getCellByPosition(1+i, offset).setFormula( "" + i + " of" );
				resize = (1+i > resize) ? 1+i : resize;
				report.getCellByPosition(2+l+i, offset).setFormula( "" + i + " of" );
				resize = (2+l+i > resize) ? 2+l+i : resize;
			}
			offset += 1;
			for(int j = 0; j < symbols.length; j++) {
				report.getCellByPosition(0, offset+j).setFormula( symbols[j] );
			}
			for(int j = 0; j < baseGameSymbolsHitFrequency.length; j++) {
				for(int i = 0, l=baseGameSymbolsHitFrequency[j].length; i < baseGameSymbolsHitFrequency[j].length; i++) {
					report.getCellByPosition(1+i, offset+j).setFormula("" + baseGameSymbolsHitFrequency[j][i]);
					resize = (1+i > resize) ? 1+i : resize;
					report.getCellByPosition(2+l+i, offset+j).setFormula("" + baseGameSymbolsHitFrequency[j][i]/(double)totalNumberOfGames);
					setDigits(document, report, 2+l+i, offset+j);
					resize = (2+l+i > resize) ? 2+l+i : resize;
				}
			}
			offset += baseGameSymbolsHitFrequency.length;
			offset += 1;

			report.getCellByPosition(0, offset).setFormula("Free Spins Symbols Frequencies:");
			offset += 1;
			for(int i = 0, l=freeSpinsSymbolsHitFrequency[0].length; i < freeSpinsSymbolsHitFrequency[0].length; i++) {
				report.getCellByPosition(1+i, offset).setFormula( "" + i + " of" );
				resize = (1+i > resize) ? 1+i : resize;
				report.getCellByPosition(2+l+i, offset).setFormula( "" + i + " of" );
				resize = (2+l+i > resize) ? 2+l+i : resize;
			}
			offset += 1;
			for(int j = 0; j < symbols.length; j++) {
				report.getCellByPosition(0, offset+j).setFormula( symbols[j] );
			}
			for(int j = 0; j < freeSpinsSymbolsHitFrequency.length; j++) {
				for(int i = 0, l=freeSpinsSymbolsHitFrequency[j].length; i < freeSpinsSymbolsHitFrequency[j].length; i++) {
					report.getCellByPosition(1+i, offset+j).setFormula("" + freeSpinsSymbolsHitFrequency[j][i]);
					resize = (1+i > resize) ? 1+i : resize;
					report.getCellByPosition(2+l+i, offset+j).setFormula("" + freeSpinsSymbolsHitFrequency[j][i]/(double)totalNumberOfGames);
					setDigits(document, report, 2+l+i, offset+j);
					resize = (2+l+i > resize) ? 2+l+i : resize;
				}
			}
			offset += freeSpinsSymbolsHitFrequency.length;
			offset += 1;

			autoSizeColums(report, resize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Macro simulation function.
	 *
	 * @param ctx Script context.
	 */
	public static void simulate(XScriptContext ctx) {
		readDataStructures(ctx);
		resetStatistics();

		for(int g=0; g<numberOfSimulations; g++) {
			long beforePlay = wonMoney;
			lostMoney += totalBet;
			singleBaseGame();
			while(freeSpinsAmount > 0) {
				singleFreeSpin();
				freeSpinsAmount--;
			}
			long roundWin = wonMoney - beforePlay;
			if(maxWin < roundWin) {
				maxWin = roundWin;
			}
		}

		repoertStatistics(ctx);
	}
}
