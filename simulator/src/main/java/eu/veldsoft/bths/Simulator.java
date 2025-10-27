package eu.veldsoft.bths;

import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.security.SecureRandom;

import com.sun.star.text.XText;
import com.sun.star.frame.XModel;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.CellContentType;
import com.sun.star.container.XIndexAccess;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.script.provider.XScriptContext;

public class Simulator {
	private static final SecureRandom PRNG = new SecureRandom();

	private static int paytable[][] = {};

	private static int lines[][] = {};

	private static int baseGameReels[][] = {};

	private static int freeSpinsReels[][] = {};

	private static int rewardFreeSpins[] = {};

	private static int freeSpinsMultipliers[] = {};

	private static int singleBet = 0;
	private static int totalBet = 0;

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

	private static long bingoLineMoney = 0L;
	private static long bingoFullHouseMoney = 0L;

	private static long bingoLineHitFrequency = 0L;
	private static long bingoFullHouseHitFrequency = 0L;

	private static long baseGameSymbolsMoney[][] = {};
	private static long baseGameSymbolsHitFrequency[][] = {};
	private static long freeSpinsSymbolsMoney[][] = {};
	private static long freeSpinsSymbolsHitFrequency[][] = {};

	//TODO Read values from the paytable spreadsheet.
	private static Set<Integer> wilds = new HashSet<>();
	private static Set<Integer> payingScatters = new HashSet<>();
	private static Set<Integer> freeSpinsTrigerScatters = new HashSet<>();

	/** Helping array for the game screen configuration. */
	private static int view[][] = {};

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

	/** Helper array for bingo cards handling. */
	private static int numbersInRow[] = {};

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

	/** Helping variable for the free spins mode as number of spins. */
	private static int freeSpinsAmount = 0;

	/** Helping variable in free spins mode for the active win multiplier. */
	private static int freeSpinsMultiplier = 0;

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
		bonuseGameHitFrequency = 0L;

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

			report.getCellByPosition(0, 0).setFormula("Total Loss:");
			report.getCellByPosition(1, 0).setFormula("" + lostMoney);

			report.getCellByPosition(0, 1).setFormula("Total Win:");
			report.getCellByPosition(1, 1).setFormula("" + wonMoney);

			/* Prevent division by zero exception. */
			lostMoney = (lostMoney == 0L ? 1L : lostMoney);

			report.getCellByPosition(0, 2).setFormula("Total RTP [%]:");
			report.getCellByPosition(1, 2).setFormula("" + ( (double)wonMoney * 100D / (double)lostMoney ) );

			report.getCellByPosition(0, 4).setFormula("Number of Games:");
			report.getCellByPosition(1, 4).setFormula("" + totalNumberOfGames);

			report.getCellByPosition(0, 5).setFormula("Number of Free Spins:");
			report.getCellByPosition(1, 5).setFormula("" + totalNumberOfFreeSpins);

			report.getCellByPosition(0, 6).setFormula("Number of Free Spins Starts:");
			report.getCellByPosition(1, 6).setFormula("" + totalNumberOfFreeSpinsStarts);

			report.getCellByPosition(0, 7).setFormula("Number of Free Spins Restarts:");
			report.getCellByPosition(1, 7).setFormula("" + totalNumberOfFreeSpinsRestarts);

			report.getCellByPosition(0, 9).setFormula("Base Gaame Win:");
			report.getCellByPosition(1, 9).setFormula("" + baseGameMoney);

			report.getCellByPosition(0, 10).setFormula("Free Spins Win:");
			report.getCellByPosition(1, 10).setFormula("" + freeSpinsMoney);

			report.getCellByPosition(0, 11).setFormula("Bonus Game Win:");
			report.getCellByPosition(1, 11).setFormula("" + bonusGameMoney);

			report.getCellByPosition(0, 13).setFormula("Base Game RTP [%]:");
			report.getCellByPosition(1, 13).setFormula("" + ( (double)baseGameMoney * 100D / (double)lostMoney ) );

			report.getCellByPosition(0, 14).setFormula("Free Spins RTP [%]:");
			report.getCellByPosition(1, 14).setFormula("" + ( (double)freeSpinsMoney * 100D / (double)lostMoney ) );

			report.getCellByPosition(0, 15).setFormula("Bonus Game RTP [%]:");
			report.getCellByPosition(1, 15).setFormula("" + ( (double)bonusGameMoney * 100D / (double)lostMoney ) );

			report.getCellByPosition(0, 17).setFormula("Max Win:");
			report.getCellByPosition(1, 17).setFormula("" + maxWin);

			report.getCellByPosition(0, 18).setFormula("Base Game Max Win:");
			report.getCellByPosition(1, 18).setFormula("" + baseGameMaxWin);

			report.getCellByPosition(0, 19).setFormula("Free Spins Max Win:");
			report.getCellByPosition(1, 19).setFormula("" + freeSpinsMaxWin);

			report.getCellByPosition(0, 20).setFormula("Bonus Game Max Win:");
			report.getCellByPosition(1, 20).setFormula("" + bonusGameMaxWin);

			report.getCellByPosition(0, 22).setFormula("Base Game Hit Frequency:");
			report.getCellByPosition(1, 22).setFormula("" + baseGameHitFrequency);

			report.getCellByPosition(0, 23).setFormula("Free Spins Hit Frequency:");
			report.getCellByPosition(1, 23).setFormula("" + freeSpinsHitFrequency);

			report.getCellByPosition(0, 24).setFormula("Bonus Game Hit Frequency:");
			report.getCellByPosition(1, 24).setFormula("" + bonuseGameHitFrequency);

			report.getCellByPosition(0, 26).setFormula("Bingo Line Win:");
			report.getCellByPosition(1, 26).setFormula("" + bingoLineMoney);

			report.getCellByPosition(0, 27).setFormula("Bingo Full House Win:");
			report.getCellByPosition(1, 27).setFormula("" + bingoFullHouseMoney);

			report.getCellByPosition(0, 29).setFormula("Bingo Line RTP [%]:");
			report.getCellByPosition(1, 29).setFormula("" + ( (double)bingoLineMoney * 100D / (double)lostMoney ) );

			report.getCellByPosition(0, 30).setFormula("Bingo Full House RTP [%]:");
			report.getCellByPosition(1, 30).setFormula("" + ( (double)bingoFullHouseMoney * 100D / (double)lostMoney ) );

			report.getCellByPosition(0, 32).setFormula("Bingo Line Hit Frequency:");
			report.getCellByPosition(1, 32).setFormula("" + bingoLineHitFrequency);

			report.getCellByPosition(0, 33).setFormula("Bingo Full House Hit Frequency:");
			report.getCellByPosition(1, 33).setFormula("" + bingoFullHouseHitFrequency);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Macro simulation function.
	 *
	 * @param ctx The script context.
	*/
	public static void simulate(XScriptContext ctx) {
		readDataStructures(ctx);
		resetStatistics();
		//TODO Game simulation logic.
		repoertStatistics(ctx);
	}
}