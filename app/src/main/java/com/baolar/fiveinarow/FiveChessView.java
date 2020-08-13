package com.baolar.fiveinarow;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class FiveChessView extends View implements View.OnTouchListener {
    final String[] pattern = {
            "11111", //五end 1e9 0
            "011110", //活四 end 1e7  1
            "01111",
            "11110",
            "10111",
            "11101",
            "11011", //死四end 1e6  6
            "001110",
            "011100",
            "011010",
            "010110", //活三end 1e5 10
            "01110",
            "00111",
            "11100",
            "11010",
            "10110",
            "01101",
            "01011",
            "11001",
            "10011",
            "10101", //死三end 1e4 20
            "011000",
            "001100",
            "000110",
            "010010",
            "010100",
            "001010", //活二end 1e3 26
            "00110",
            "01100",
            "00011",
            "11000",
            "01010",
            "10100",
            "10010",
            "10001",
            "01001",
            "00101",//死二end  1e2 36
            "001000",
            "000100",
            "010000",
            "000010", //活一end   10 40
            "00100",
            "01000",
            "00010",
            "10000",
            "00001" //死一end   1 45
    };

    final double score[] = {
            1e12,
            1e11,
            1e9 + 2,
            1e9 + 2,
            1e9 + 1,
            1e9 + 1,
            1e9,
            6e8 + 1,
            6e8 + 1,
            6e8,
            6e8,
            1e7 + 4,
            1e7 + 3,
            1e7 + 3,
            1e7 + 2,
            1e7 + 2,
            1e7 + 2,
            1e7 + 2,
            1e7 + 1,
            1e7 + 1,
            1e7,
            1e6 + 2,
            1e6 + 2,
            1e6 + 2,
            1e6 + 1,
            1e6 + 1,
            1e6,
            1e5 + 2,
            1e5 + 2,
            1e5 + 1,
            1e5 + 1,
            1e5,
            1e5,
            1e5,
            1e5,
            1e5,
            1e5,
            1e3,
            1e3,
            1e3,
            1e3,
            1e1 + 1,
            1e1 + 1,
            1e1 + 1,
            1e1,
            1e1
    };

    final int weight[][]= { //位置权重，大小为boardsz + 1 * boardsz + 1
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1},
            {0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 6, 7, 7, 7, 6, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 6, 7, 7, 7, 6, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 4, 3, 2, 1},
            {0, 1, 2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 2, 1},
            {0, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1},
            {0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    //画笔
    private Paint paint;
    //游戏是否结束
    private boolean isGameOver = false;
    //bitmap
    private Bitmap whiteChess;
    private Bitmap blackChess;
    //Rect
    private Rect rect;
    //棋盘宽高
    private float len;

    //每格之间的距离
    private float preWidth;
    //边距
    private float offset;
    //是否是玩家的回合
    private boolean isUserBout = true;

    //白棋
    public static final int WHITE = 1;
    //黑棋
    public static final int BLACK = 2;
    //无棋
    public static final int NOTUSED = 0;

    private final int DEFENDWEIGHT = 2;
    //棋盘格数
    private int boardsz = 15;
    //棋子数组
    private int[][] board;

    private final int patnum = 46;

    public FiveChessView(Context context) {
        this(context, null);
    }

    public FiveChessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FiveChessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化Paint
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        //初始化board
        board = new int[boardsz + 1][boardsz + 1];
        //初始化棋子图片bitmap
        whiteChess = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_chess);
        blackChess = BitmapFactory.decodeResource(context.getResources(), R.drawable.black_chess);
        //初始化胜利局数
        //初始化Rect
        rect = new Rect();
        //设置点击监听
        setOnTouchListener(this);
        //重置棋盘状态
        for (int i = 1; i <= boardsz; i++) {
            for (int j = 1; j <= boardsz; j++) {
                board[i][j] = 0;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取高宽值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //获取宽高中较小的值
        int len = width > height ? height : width;
        //重新设置宽高
        setMeasuredDimension(len, len);
    }

    @Override //绘制棋盘，实现onDraw接口，所以后面可以直接调用postInvalidate();更新界面。
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //棋盘为一个boardsz*boardsz的正方形，所有棋盘宽高必须一样
        len = getWidth() > getHeight() ? getHeight() : getWidth();
        preWidth = len / boardsz;
        //边距
        offset = preWidth / 2;
        //棋盘线条
        for (int i = 0; i < boardsz; i++) {
            float start = i * preWidth + offset;
            //横线
            canvas.drawLine(offset, start, len - offset, start, paint);
            //竖线
            canvas.drawLine(start, offset, start, len - offset, paint);
        }
        //绘制棋子
        for (int i = 0; i < boardsz; i++) {
            for (int j = 0; j < boardsz; j++) {
                //rect中点坐标
                float rectX = offset + i * preWidth;
                float rectY = offset + j * preWidth;
                //设置rect位置
                rect.set((int) (rectX - offset), (int) (rectY - offset),
                        (int) (rectX + offset), (int) (rectY + offset));
                //遍历board
                switch (board[i + 1][j + 1]) {
                    case WHITE:
                        //绘制白棋
                        canvas.drawBitmap(whiteChess, null, rect, paint);
                        break;
                    case BLACK:
                        //绘制黑棋
                        canvas.drawBitmap(blackChess, null, rect, paint);
                        break;
                }
            }
        }
    }

    /**
     * 重置游戏
     */
    public void resetGame() {
        isGameOver = false;
        //重置棋盘状态
        for (int i = 1; i <= boardsz; i++) {
            for (int j = 1; j <= boardsz; j++) {
                board[i][j] = NOTUSED;
            }
        }
        //更新UI
        postInvalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!isGameOver) {
                    //获取按下时的位置
                    float downX = event.getX();
                    float downY = event.getY();
                    //点击的位置在棋盘上
                    if (downX >= offset / 2 && downX <= len - offset / 2 && downY >= offset / 2 && downY <= len - offset / 2) {
                        //获取棋子对应的位置
                        int x = (int) (downX / preWidth) + 1;
                        int y = (int) (downY / preWidth) + 1;
                        //判断当前位置是否已经有子
                        if (board[x][y] == NOTUSED) {
                            if(isUserBout) {
                                //给数组赋值
                                board[x][y] = BLACK;
                                postInvalidate();

                                long res = getTotVal(BLACK);

                                if(res >= getVal(0)) {
                                    Toast.makeText(getContext(), "恭喜你战胜了Alpha Five！",
                                            Toast.LENGTH_SHORT).show();
                                    isGameOver = true;
                                    break;
                                }

                                isUserBout = false;

                                Point p = getOptimal(WHITE);
                                play(WHITE, p);
                                //更新棋盘 刷新整个View
                                postInvalidate();

                                res = getTotVal(WHITE);

                                if(res >= getVal(0)) {
                                    Toast.makeText(getContext(), "Alpha Five 赢了！",
                                            Toast.LENGTH_LONG).show();
                                    isGameOver = true;
                                }

                                isUserBout = true;
                            }

                        }
                    }
                }
                else  {
                    Toast.makeText(getContext(), "游戏已经结束，请重新开始！",
                            Toast.LENGTH_LONG).show();
                }

                break;
        }

        return false;
    }

    public double getVal(int p) {
//        if (p == 0) return 1e12 + 0 - p; //5
//        else if (p == 1) return 1e11 + 1 - p;//活4
//        else if (2 <= p && p <= 6) return 1e9 + 6 - p;//死4
//        else if (7 <= p && p <= 10) return 6e8 + 10 - p;//活3
//        else if (11 <= p && p <= 20) return 1e7 + 20 - p;//死3
//        else if (21 <= p && p <= 26) return 1e6 + 26 - p;//活2
//        else if (27 <= p && p <= 36) return 1e5 + 36 - p;//死2
//        else if (37 <= p && p <= 40) return 1e3 + 40 - p;//活1
//        else  if (41 <= p && p <= 45) return 1e1 + 45 - p;//死1
        return score[p];
    }

    int getPatCount(int ply, final String str) {
        int cnt = 0;
        int n = boardsz;

        boolean d = (ply - 1 == 1) ? true : false;

        for (int i = 1; i <= n; i++) { //行
            for (int j = 1; j <= n - str.length() + 1; j++) {
                boolean flag = true;
                for (int k = 0; k < str.length(); k++) {
                    if (str.charAt(k) - '0' + ((d && str.charAt(k) == '1') ? 1:0)!= board[i][j + k]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) cnt++;
            }
        }

        for (int j = 1; j <= n; j++) { //列
            for(int i = 1; i <= n - str.length() + 1; i++) {
                boolean flag = true;
                for (int k = 0; k < str.length(); k++) {
                    if (str.charAt(k) - '0' + ((d && str.charAt(k) == '1') ? 1:0) != board[i + k][j]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) cnt++;
            }
        }

        for (int t = 1; t <= n - str.length() + 1; t++) { //左开始斜向下
            for (int i = t, j = 1; i <= n - str.length() + 1; i++, j++) {
                boolean flag = true;
                for (int k = 0; k < str.length(); k++) {
                    if (str.charAt(k) - '0' + ((d && str.charAt(k) == '1') ? 1:0) != board[i + k][j + k]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) cnt++;
            }
        }

        for (int t = 2; t <= n - str.length() + 1; t++) { //从上开始斜向下
            for (int j = t, i = 1; j <= n - str.length() + 1; j++, i++) {
                boolean flag = true;
                for (int k = 0; k < str.length(); k++) {
                    if (str.charAt(k) - '0' + ((d && str.charAt(k) == '1') ? 1:0) != board[i + k][j + k]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) cnt++;
            }
        };

        for (int t = str.length(); t <= n; t++) { //从左开始斜向上
            for (int i = t, j = 1; i >= str.length(); i--, j++) {
                boolean flag = true;
                for (int k = 0; k < str.length(); k++) {
                    if (str.charAt(k) - '0' + ((d && str.charAt(k) == '1') ? 1:0) != board[i - k][j + k]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) cnt++;
            }
        }

        for (int t = 2; t <= n - str.length() + 1; t++) { //从下开始斜向上
            for (int j = t, i = n; j <= n - str.length() + 1; j++, i--) {
                boolean flag = true;
                for (int k = 0; k < str.length(); k++) {
                    if (str.charAt(k) - '0' + ((d && str.charAt(k) == '1') ? 1:0) != board[i - k][j + k]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) cnt++;
            }
        }

        return cnt;
    }

    int[] getAllCount(int ply) {
        int[]ans = new int[patnum]; //初始化每种情形的权值

        for (int i = 0; i < patnum; i++)
            ans[i] = getPatCount(ply, pattern[i]);

        return ans;
    }

    long getTotVal(int ply) {
        int[]cnt = getAllCount(ply);
        long ans = 0;

        for (int i = 0; i < cnt.length; i++) {
            ans += cnt[i] * (long)getVal(i);
        }

        return ans;
    }

    boolean play(int ply, final Point p) {
        if (board[p.x][p.y] == NOTUSED && 1 <= p.x && p.x <= boardsz && 1 <= p.y && p.y <= boardsz)  {
            board[p.x][p.y] = ply;
            return true;
        }

        return false;
    }

    int getRival(int ply) {
        if(ply == WHITE) return BLACK;
        return WHITE;
    }

    Point getOptimal(int ply) {
        int n = boardsz;
        long _max = -1;
        long[][]values = new long[boardsz + 1][boardsz + 1];
        long[][]my_values = new long[boardsz + 1][boardsz + 1];
        long[][]rival_values = new long[boardsz + 1][boardsz + 1];
        long my_max = -1;
        long rival_max = -1;
        List<Point> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                if(board[i][j] != NOTUSED) continue;

                play(ply, new Point(i, j)); //假设己方下
                long tmp = getTotVal(ply);
                my_values[i][j] = tmp; //将己方权重存在my_values中

                if(tmp > my_max) my_max = tmp;

                board[i][j] = NOTUSED; //还原
                if(tmp > getVal(0)) //如果己方下了赢了，就直接下这个子
                    return new Point(i, j);

                //假设对手下
                play(getRival(ply), new Point(i, j));

                long tmptotval = 0;

                for(int k = 0; k < 22; k++) {
                    tmptotval += getPatCount(getRival(ply), pattern[k]) * (long)getVal(k);
                }

                tmp += tmptotval * DEFENDWEIGHT;

                for(int k = 23; k < patnum; k++) {
                    tmptotval += getPatCount(getRival(ply), pattern[k]) * (long)getVal(k);
                }

                rival_values[i][j] = tmptotval;

                if(rival_values[i][j] > rival_max) rival_max = rival_values[i][j]; //维护对手最大值

                board[i][j] = NOTUSED; //还原

                values[i][j] = tmp; //存权重 全部的权重
                if(values[i][j] > _max)
                    _max = values[i][j];  //_max维护最大当前权值。
            }
        }

        if(my_max >= rival_max) {
            values = my_values;
            _max = my_max;
            Toast.makeText(getContext(), "AI加权判断：Alpha Five 进攻！",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "AI加权判断：Alpha Five 综合攻防！",
                    Toast.LENGTH_SHORT).show();
        }

        long _max_pos = -1;
        for(int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if(board[i][j] != NOTUSED) continue;

                if (values[i][j] == _max) {
                    if(values[i][j] + weight[i][j]> _max_pos) {
                        _max_pos = values[i][j] + weight[i][j];
                    }
                }
            }
        }

        for(int i = 1; i <= n; i++) {
            for(int j = 1; j <= n; j++) {
                if(board[i][j] != NOTUSED) continue;

                if(values[i][j] == _max && values[i][j] + weight[i][j] == _max_pos) {
                    points.add(new Point(i, j));
                }
            }
        }

        int pos = (int)(Math.random() * points.size());

        return new Point(points.get(pos));
    }
}
