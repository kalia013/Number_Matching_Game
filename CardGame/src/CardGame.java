import java.util.*;

public class CardGame {

    private static int unmatchedCards;
    private static final int ROW = 3;
    private static final int COL = 6;
    private static final int[][] board = new int[ROW][COL];
    private static final boolean[][] revealed = new boolean[ROW][COL];
    private static final boolean[][] matched = new boolean[ROW][COL];

    public static void main(String[] args) {
        initializeBoard();
        printBoard();
        playGame();
    }

    private static void initializeBoard() {
        List<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < 3; j++) {
                deck.add(i);
            }
        }
        Collections.shuffle(deck);

        unmatchedCards = deck.size() % 2;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                board[i][j] = deck.remove(deck.size() - 1);
            }
        }
    }


    private static void playGame() {
        Scanner scanner = new Scanner(System.in);
        int tries = 1;
        int remainingCards = ROW * COL;

        while (remainingCards > unmatchedCards) {
            System.out.println();
            System.out.printf("<시도 %d, 남은 카드: %d> 좌표를 두 번 입력하세요. (예시: (1, 3))\n", tries, remainingCards);
            int[] first = getCoords(scanner, "입력 1? ");
            int[] second;

            do {
                second = getCoords(scanner, "입력 2? ");
                if (Arrays.equals(first, second)) {
                    System.out.println("동일한 좌표가 입력되었습니다. 다른 좌표를 입력하세요.");
                }
            } while (Arrays.equals(first, second));

            // 두 좌표에 있는 카드를 잠시 뒤집어 보여줍니다.
            revealed[first[0]][first[1]] = true;
            revealed[second[0]][second[1]] = true;
            printBoard();


            if (board[first[0]][first[1]] == board[second[0]][second[1]]) {
                // 맞추어진 카드를 표시합니다.
                matched[first[0]][first[1]] = true;
                matched[second[0]][second[1]] = true;
                remainingCards -= 2;
                System.out.println("\n짝을 맞췄습니다!");
            } else {
                // 두 카드가 일치하지 않으면 다시 뒤집어 둡니다.
                revealed[first[0]][first[1]] = false;
                revealed[second[0]][second[1]] = false;
                System.out.println("\n짝을 맞추지 못했습니다! 다시 시도해보세요!");
            }
            tries++;
            if (noMatchesLeft()) {
                System.out.println("짝을 이룰 수 있는 카드가 더 이상 없습니다. 게임을 종료합니다.");
                printAllBoard();
                return;
            }
        }
        System.out.println("축하합니다! 게임에서 승리하셨습니다!");
    }


    private static void printBoard() {

        System.out.println();


        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (matched[i][j]) {
                    System.out.print("  "); // 맞추어진 카드는 공백으로 표시
                } else if (revealed[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }

    private static void printAllBoard() { // 남은 카드를 보여주는 함수

        System.out.println();


        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (matched[i][j]) {
                    System.out.print("  "); // 맞추어진 카드는 공백으로 표시
                } else if (revealed[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
    }

    private static int[] getCoords(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            input = input.trim();
            if (input.startsWith("(") && input.endsWith(")")) {
                input = input.substring(1, input.length() - 1);
                String[] parts = input.split(",");
                if (parts.length == 2) {
                    try {
                        int row = Integer.parseInt(parts[0].trim()) - 1;
                        int col = Integer.parseInt(parts[1].trim()) - 1;
                        if ((row >= 0 && row < ROW) && (col >= 0 && col < COL)) {
                            if (!revealed[row][col]) {
                                return new int[]{row, col};
                            } else {
                                System.out.println("이미 맞춰진 카드입니다. 다른 좌표를 입력하세요.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        // 입력값이 숫자가 아닌 경우, 아래의 오류 메시지를 출력합니다.
                    }
                }
            }
            System.out.println("유효한 좌표를 입력하세요. 예) (1, 3) / (1, 1) ~ (3, 6)");
        }
    }

    private static boolean noMatchesLeft() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (!matched[i][j]) {
                    for (int k = 0; k < ROW; k++) {
                        for (int l = 0; l < COL; l++) {
                            if ((i != k || j != l) && !matched[k][l] && board[i][j] == board[k][l]) {
                                // 짝이 되는 카드가 있음
                                return false;
                            }
                        }
                    }
                }
            }
        }
        // 짝이 되는 카드가 없음
        return true;
    }
}
