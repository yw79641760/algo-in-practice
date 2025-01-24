# 华容道游戏

## 游戏规则

华容道是中国传统的滑块类益智游戏，目标是将曹操从出口移出。

### 棋盘布局
- 棋盘大小：5行4列
- 棋子类型：
  - 曹操 (2x2)
  - 张飞 (2x1)
  - 关羽 (1x2)
  - 马超 (2x1)
  - 赵云 (2x1)
  - 黄忠 (2x1)
  - 小兵 (1x1)

### 胜利条件
曹操需要完全移动到棋盘最后两行的中间两列位置，即：
- 第4行第2列
- 第4行第3列
- 第5行第2列
- 第5行第3列

## 游戏玩法

1. 运行游戏：
```bash
javac huarongdao/HuaRongDao.java
java huarongdao.HuaRongDao
```

2. 游戏会自动寻找解决方案

3. 解决方案将显示：
   - 每一步的移动步骤
   - 最终棋盘状态

## 代码说明

### 主要类
- `HuaRongDao`：主游戏类，包含游戏逻辑

### 主要方法
- `isGoal()`：判断是否达到胜利条件
- `move()`：处理棋子移动
- `solve()`：使用BFS算法寻找解决方案

## 初始布局

```java
int[][] initialBoard = {
    {CAOCAO, CAOCAO, ZHANGFEI, EMPTY},
    {CAOCAO, CAOCAO, ZHANGFEI, EMPTY},
    {MACHUO, MACHUO, SOLDIER, SOLDIER},
    {GUANYU, GUANYU, SOLDIER, SOLDIER},
    {EMPTY, EMPTY, EMPTY, EMPTY}
};
```

## 注意事项
- 如果显示"No solution found"，请检查初始布局是否正确
- 确保所有棋子尺寸和位置符合规则
