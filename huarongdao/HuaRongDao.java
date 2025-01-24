package huarongdao;
import java.util.*;

class HuaRongDao {
    // 移动方向, 上下左右
    private static final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    // 棋子类型
    private static final int EMPTY = 0;
    private static final int CAOCAO = 1;  // 曹操 2x2
    private static final int ZHANGFEI = 2; // 张飞 2x1
    private static final int GUANYU = 3;  // 关羽 1x2
    private static final int MACHUO = 4;  // 马超 2x1
    private static final int ZHAOYUN = 5; // 赵云 2x1
    private static final int HUANGZHONG = 6; // 黄忠 2x1
    private static final int SOLDIER = 7; // 小兵 1x1

    // 棋盘大小
    private static final int ROWS = 5;
    private static final int COLS = 4;

    // 棋盘表示
    private int[][] board;

    public HuaRongDao(int[][] board) {
        this.board = board.clone();
    }

    private boolean isGoal(int[][] board) {
        // 检查曹操是否完全位于出口位置（最后两行的中间两列）
        for (int i = ROWS - 2; i < ROWS; i++) {
            for (int j = 1; j < 3; j++) {
                if (board[i][j] != CAOCAO) {
                    return false;
                }
            }
        }
        return true;
    }

    // 棋子尺寸
    private static final int[][] pieceSizes = {
        {0, 0}, // EMPTY
        {2, 2}, // CAOCAO
        {2, 1}, // ZHANGFEI
        {1, 2}, // GUANYU
        {2, 1}, // MACHUO
        {2, 1}, // ZHAOYUN
        {2, 1}, // HUANGZHONG
        {1, 1}  // SOLDIER
    };

    private int[][] move(int[][] board, int rowDir, int colDir) {
        int emptyRow = findEmptyRow(board);
        int emptyCol = findEmptyCol(board);

        // 检查空位是否有效
        if (emptyRow == -1 || emptyCol == -1) {
            return null;
        }

        // 计算要移动的棋子的位置
        int pieceRow = emptyRow - rowDir;
        int pieceCol = emptyCol - colDir;

        // 检查棋子位置是否在边界内
        if (pieceRow < 0 || pieceRow >= ROWS || pieceCol < 0 || pieceCol >= COLS) {
            return null;
        }

        int piece = board[pieceRow][pieceCol];
        int[] size = pieceSizes[piece];
        
        // 检查移动是否合法
        if (rowDir != 0) { // 上下移动
            // 检查整个棋子区域
            for (int i = 0; i < size[0]; i++) {
                for (int j = 0; j < size[1]; j++) {
                    if (pieceRow + i < 0 || pieceRow + i >= ROWS ||
                        pieceCol + j < 0 || pieceCol + j >= COLS ||
                        board[pieceRow + i][pieceCol + j] != piece) {
                        return null;
                    }
                }
            }
        } else { // 左右移动
            // 检查整个棋子区域
            for (int i = 0; i < size[0]; i++) {
                for (int j = 0; j < size[1]; j++) {
                    if (pieceRow + i < 0 || pieceRow + i >= ROWS ||
                        pieceCol + j < 0 || pieceCol + j >= COLS ||
                        board[pieceRow + i][pieceCol + j] != piece) {
                        return null;
                    }
                }
            }
        }

        int[][] newBoard = cloneBoard(board);
        // 移动整个棋子
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                swap(newBoard, emptyRow + i, emptyCol + j, pieceRow + i, pieceCol + j);
            }
        }
        return newBoard;
    }

    private int findEmptyRow(int[][] board) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == EMPTY) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findEmptyCol(int[][] board) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == EMPTY) {
                    return j;
                }
            }
        }
        return -1;
    }

    private void swap(int[][] board, int r1, int c1, int r2, int c2) {
        int temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
    }

    private int[][] cloneBoard(int[][] board) {
        int[][] newBoard = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, COLS);
        }
        return newBoard;
    }

    // 记录移动步骤
    private List<String> steps = new ArrayList<>();

    public boolean solve() {
        Queue<int[][]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(board);
        visited.add(Arrays.deepToString(board));

        while (!queue.isEmpty()) {
            int[][] currentBoard = queue.poll();

            if (isGoal(currentBoard)) {
                this.board = currentBoard;  // 保存最终棋盘状态
                printSolution();
                printFinalBoard();
                return true;
            }

            for (int[] dir : directions) {
                int[][] nextBoard = move(currentBoard, dir[0], dir[1]);
                if (nextBoard != null && !visited.contains(Arrays.deepToString(nextBoard))) {
                    queue.add(nextBoard);
                    visited.add(Arrays.deepToString(nextBoard));
                    steps.add(getMoveDescription(currentBoard, nextBoard));
                }
            }
        }

        System.out.println("No solution found.");
        return false;
    }

    private String getMoveDescription(int[][] from, int[][] to) {
        // 找出移动的棋子
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (from[i][j] != to[i][j]) {
                    int piece = from[i][j] != EMPTY ? from[i][j] : to[i][j];
                    // Calculate new position ensuring it stays within bounds
                    int newRow = Math.max(0, Math.min(ROWS-1, i + (from[i][j] == EMPTY ? 1 : -1)));
                    int newCol = Math.max(0, Math.min(COLS-1, j + (from[i][j] == EMPTY ? 1 : -1)));
                    return String.format("Move %-8s from (%d,%d) to (%d,%d)",
                        getPieceName(piece), i, j, newRow, newCol);
                }
            }
        }
        return "Unknown move";
    }

    private String getPieceName(int piece) {
        switch (piece) {
            case CAOCAO: return "CaoCao";
            case ZHANGFEI: return "ZhangFei";
            case GUANYU: return "GuanYu";
            case MACHUO: return "MaChuo";
            case ZHAOYUN: return "ZhaoYun";
            case HUANGZHONG: return "HuangZhong";
            case SOLDIER: return "Soldier";
            case EMPTY: return "Empty";
            default: return "Unknown";
        }
    }

    private void printSolution() {
        System.out.println("Solution steps:");
        for (int i = 0; i < steps.size(); i++) {
            System.out.println((i + 1) + ". " + steps.get(i));
        }
    }

    private void printFinalBoard() {
        System.out.println("\nFinal board state:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(getPieceName(board[i][j]) + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // 经典可解的4列华容道初始布局
int[][] initialBoard = {
    {ZHANGFEI, CAOCAO, CAOCAO, MACHUO},
    {ZHANGFEI, CAOCAO, CAOCAO, MACHUO},
    {HUANGZHONG, EMPTY, EMPTY, ZHAOYUN},
    {HUANGZHONG, GUANYU, GUANYU, ZHAOYUN},
    {SOLDIER, SOLDIER, SOLDIER, SOLDIER}
};

        HuaRongDao game = new HuaRongDao(initialBoard);
        game.solve();
    }
}
