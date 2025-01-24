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
    private static final int SOLDIER = 4; // 小兵 1x1

    // 棋盘大小
    private static final int ROWS = 4;
    private static final int COLS = 5;

    // 棋盘表示
    private int[][] board;

    public HuaRongDao(int[][] board) {
        this.board = board.clone();
    }

    private boolean isGoal(int[][] board) {
        // 目标位置是最后一行的第一列
        return board[ROWS - 1][0] == CAOCAO;
    }

    // 棋子尺寸
    private static final int[][] pieceSizes = {
        {0, 0}, // EMPTY
        {2, 2}, // CAOCAO
        {2, 1}, // ZHANGFEI
        {1, 2}, // GUANYU
        {1, 1}  // SOLDIER
    };

    private int[][] move(int[][] board, int rowDir, int colDir) {
        int emptyRow = findEmptyRow(board);
        int emptyCol = findEmptyCol(board);

        // 检查空位是否有效
        if (emptyRow == -1 || emptyCol == -1) {
            return null;
        }

        int newRow = emptyRow + rowDir;
        int newCol = emptyCol + colDir;

        // 检查新位置是否在边界内
        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
            return null;
        }

        int piece = board[newRow][newCol];
        int[] size = pieceSizes[piece];
        
        // 检查移动是否合法
        if (rowDir != 0) { // 上下移动
            // 检查新位置是否在边界内
            if (newRow + size[0] - 1 >= ROWS) {
                return null;
            }
            // 宽棋子需要检查整列
            for (int i = 0; i < size[1]; i++) {
                if (newCol + i >= COLS || board[newRow][newCol + i] != piece) {
                    return null;
                }
            }
        } else { // 左右移动
            // 检查新位置是否在边界内
            if (newCol + size[1] - 1 >= COLS) {
                return null;
            }
            // 高棋子需要检查整行
            for (int i = 0; i < size[0]; i++) {
                if (newRow + i >= ROWS || board[newRow + i][newCol] != piece) {
                    return null;
                }
            }
        }

        int[][] newBoard = cloneBoard(board);
        // 移动整个棋子
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                swap(newBoard, emptyRow + i, emptyCol + j, newRow + i, newCol + j);
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
        // 标准华容道初始布局
        int[][] initialBoard = {
            {CAOCAO, CAOCAO, ZHANGFEI, ZHANGFEI, GUANYU},
            {CAOCAO, CAOCAO, ZHANGFEI, ZHANGFEI, GUANYU},
            {SOLDIER, SOLDIER, SOLDIER, SOLDIER, EMPTY},
            {SOLDIER, SOLDIER, SOLDIER, SOLDIER, EMPTY}
        };

        HuaRongDao game = new HuaRongDao(initialBoard);
        game.solve();
    }
}
