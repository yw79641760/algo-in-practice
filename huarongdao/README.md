# 华容道求解器

本Java程序使用广度优先搜索算法解决经典华容道拼图。

## 拼图描述
华容道是一种滑块拼图，目标是将最大的方块（曹操）移动到棋盘的左下角。拼图包含：
- 1个 2x2 方块（曹操）
- 2个 2x1 方块（张飞）
- 1个 1x2 方块（关羽）
- 4个 1x1 方块（士兵）

## 算法概述
求解器采用广度优先搜索（BFS）算法：
1. **初始化**：从初始棋盘配置开始
2. **状态表示**：每个棋盘状态用二维数组表示
3. **移动生成**：为每个状态生成所有可能的有效移动
4. **状态跟踪**：使用队列管理状态，集合记录已访问状态
5. **目标检查**：检查曹操是否到达左下角
6. **解决方案路径**：记录并显示移动序列

## Implementation Details
- **Board Representation**: 4x5 grid with integer codes for each piece
- **Piece Types**: 
  - 0: Empty
  - 1: CaoCao (2x2)
  - 2: ZhangFei (2x1)
  - 3: GuanYu (1x2)
  - 4: Soldier (1x1)
- **Move Validation**: 
  - Checks piece boundaries
  - Ensures moves stay within board limits
  - Validates piece integrity during movement

## Key Methods
- `move()`: Generates new board states by moving pieces
- `isGoal()`: Checks if CaoCao is in the target position
- `solve()`: Implements the BFS algorithm
- `getMoveDescription()`: Creates human-readable move descriptions

## Usage
Compile and run the program:
```bash
javac huarongdao/HuaRongDao.java
java -cp . huarongdao.HuaRongDao
```

The program will output:
1. Sequence of moves to solve the puzzle
2. Final board configuration

## Optimization
- Uses BFS to guarantee the shortest solution
- Maintains a visited set to avoid redundant states
- Efficient board cloning and state comparison
