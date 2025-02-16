import java.util.Arrays;

public class TianJiSaiMa {
    // 计算田忌赛马的最大胜利场次
    public static int maxWins(int[] tianji, int[] king) {
        // 对双方马匹速度进行排序
        Arrays.sort(tianji);
        Arrays.sort(king);
        
        int wins = 0;
        int tLow = 0, tHigh = tianji.length - 1; // 田忌马匹指针
        int kLow = 0, kHigh = king.length - 1;   // 国王马匹指针
        
        // 贪心算法匹配马匹
        while (tLow <= tHigh) {
            if (tianji[tHigh] > king[kHigh]) {
                // 田忌最好的马比国王最好的马快
                wins++;
                tHigh--;
                kHigh--;
            } else if (tianji[tLow] > king[kLow]) {
                // 田忌最差的马比国王最差的马快
                wins++;
                tLow++;
                kLow++;
            } else {
                // 用田忌最差的马消耗国王最好的马
                if (tianji[tLow] < king[kHigh]) {
                    // 如果田忌最差的马确实比国王最好的马慢
                    // 则这局必输，但可以消耗对方最好的马
                    tLow++;
                    kHigh--;
                } else {
                    // 如果速度相等，则平局
                    tLow++;
                    kHigh--;
                }
            }
        }
        return wins;
    }

    public static void main(String[] args) {
        // 测试用例
        int[] tianji = new int[100];
        int[] king = new int[100];
        
        // 随机生成马匹速度（示例）
        for (int i = 0; i < 100; i++) {
            tianji[i] = (int)(Math.random() * 100);
            king[i] = (int)(Math.random() * 100);
        }
        
        System.out.println("田忌马匹速度: " + Arrays.toString(tianji));
        System.out.println("国王马匹速度: " + Arrays.toString(king));
        System.out.println("最大胜利场次: " + maxWins(tianji, king));
    }
}
