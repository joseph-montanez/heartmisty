package com.gorilla3d.app;
 
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
 
public class HeartMisty extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new Panel(this));
    }
    
    class Misty {
        int health = 100;
        int x = 32;
        int y = 128;
        double vx = 0.00;
        double vy = 0.00;
    }
    
    class Block {
        int resourceId;
        int x;
        int y;
        double viscosity;
        boolean blocking;
    }
    
    class Grass extends Block {
        public Grass() {
            resourceId = R.drawable.grass;
            blocking = true;
        }
    }
    
    class Water extends Block {
        public Water() {
            resourceId = R.drawable.water;
            blocking = false;
            viscosity = 0.25;
        }
    }
    
    class Air extends Block {
        public Air() {
            resourceId = R.drawable.air;
            blocking = false;
        }
    }
 
    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private TutorialThread _thread;
        private int _x = 20;
        private int _y = 20;
        private int frames = 0;
        private String fps = "0";
        private long t = System.currentTimeMillis(); 
        private StringBuilder log = new StringBuilder("");
        private Block[][] map = new Block[10][6];
        
        private String direction = "";
        private String button = "";
        
        private int canvasHeight = 100; 
        private int canvasWidth = 320; 
        
        private Misty misty = new Misty();
        
        long logicTs = System.currentTimeMillis();
 
        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            
            map[0][0] = new Air();
            map[0][1] = new Air();
            map[0][2] = new Air();
            map[0][3] = new Air();
            map[0][4] = new Grass();
            map[0][5] = new Water();
            
            map[1][0] = new Air();
            map[1][1] = new Air();
            map[1][2] = new Air();
            map[1][3] = new Air();
            map[1][4] = new Grass();
            map[1][5] = new Water();
            
            map[2][0] = new Air();
            map[2][1] = new Air();
            map[2][2] = new Air();
            map[2][3] = new Grass();
            map[2][4] = new Grass();
            map[2][5] = new Water();
            
            map[3][0] = new Air();
            map[3][1] = new Air();
            map[3][2] = new Air();
            map[3][3] = new Air();
            map[3][4] = new Grass();
            map[3][5] = new Water();
            
            map[4][0] = new Air();
            map[4][1] = new Air();
            map[4][2] = new Air();
            map[4][3] = new Air();
            map[4][4] = new Grass();
            map[4][5] = new Water();
            
            map[5][0] = new Air();
            map[5][1] = new Air();
            map[5][2] = new Air();
            map[5][3] = new Air();
            map[5][4] = new Grass();
            map[5][5] = new Water();
            
            map[6][0] = new Air();
            map[6][1] = new Air();
            map[6][2] = new Air();
            map[6][3] = new Air();
            map[6][4] = new Grass();
            map[6][5] = new Water();
            
            map[7][0] = new Air();
            map[7][1] = new Air();
            map[7][2] = new Air();
            map[7][3] = new Air();
            map[7][4] = new Grass();
            map[7][5] = new Water();
            
            map[8][0] = new Air();
            map[8][1] = new Air();
            map[8][2] = new Air();
            map[8][3] = new Air();
            map[8][4] = new Water();
            map[8][5] = new Water();
            
            map[9][0] = new Air();
            map[9][1] = new Air();
            map[9][2] = new Air();
            map[9][3] = new Air();
            map[9][4] = new Water();
            map[9][5] = new Water();
            
            _thread = new TutorialThread(getHolder(), this);
            
            new Thread(new Runnable() {
                public void run() {
                    while(true) {
                        updateLogic();
                        try {
                            Thread.sleep(40);
                        } catch(InterruptedException e) {
                            
                        }
                    }
                }
            }).start();
        }
        
        public void updateLogic() {
            int x = 0;
            int y = 0;
            if(direction == "right") {
                x += 4;
            } else if(direction == "left") {
                x -= 4;
            } else if(direction == "up") {
                y += 4;
            }
            
            if(misty.y > 0) {
                y -= 1;
            }
            if(misty.y < 0) {
                y = 0;
            }
            
            if(!isBlocked(new int[] {misty.x + x, misty.y + y})) {
                misty.x += x;
                misty.y += y;
            }
        }
 
        @Override
        public void onDraw(Canvas canvas) {
            Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
            Bitmap button = BitmapFactory.decodeResource(getResources(), R.drawable.button);
            Bitmap dpad = BitmapFactory.decodeResource(getResources(), R.drawable.d_pad);
            Bitmap character = BitmapFactory.decodeResource(getResources(), R.drawable.character);
            canvas.drawColor(Color.BLACK);
            //java.util.Random randomGenerator = new java.util.Random();
            //canvas.drawBitmap(_scratch, 0, 0, null);
            canvas.drawBitmap(_scratch, _x, _y, null);
            canvas.drawBitmap(button, 410, 200, null);
            canvas.drawBitmap(button, 350, 232, null);
            canvas.drawBitmap(dpad, 10, 162, null);
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setColor(0xFFFF0000);
            /*
            String id = Integer.toString(R.drawable.grass);
            canvas.drawText(id, 0, id.length(), (float) 100.0, (float) 100.0, paint);
            */
            // Get the max tile width density
            int controllerHeight = 132;
            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();
            int maxY = (int) Math.floor((double) ((canvas.getHeight() - controllerHeight) / 32));
            int maxX = (int) Math.floor((double) (canvas.getHeight() / 32));
            //android.util.Log.i("MaxY", Integer.toString(maxY));
            java.util.HashMap<Integer, Bitmap> tiles = new java.util.HashMap<Integer, Bitmap>();
            int mapWidth;
            int mapHeight;
            
            mapWidth = maxX < map.length ? maxX : map.length;
            for(int x = 0; x < mapWidth; x++) {
                mapHeight = maxY < map[x].length ? maxY : map[x].length;
                for(int y = 0; y < mapHeight; y++) {
                    Block block = (Block) map[x][y];
                    int tileId = block.resourceId;
                    Bitmap tile;
                    // If they bitmap was already constructed then use the cache
                    if(tiles.containsKey(new Integer(tileId))) {
                        tile = tiles.get(new Integer(tileId));
                    } else {
                        // There is no cache so create a new one
                        tile = BitmapFactory.decodeResource(getResources(), tileId);
                        tiles.put(new Integer(tileId), tile);
                    }
                    canvas.drawBitmap(tile, (float) x * 32, (float) y * 32, null);
                }
            }
            long now = System.currentTimeMillis();
            if(now - t >= 1000) {
                t = System.currentTimeMillis();
                fps = Integer.toString(frames);
                fps += " fps";
                frames = 0;
            }
            frames++; 
            canvas.drawText(fps, 0, fps.length(), (float) 200.0, (float) 100.0, paint);
            canvas.drawText(log.toString(), 0, log.length(), (float) 200.0, (float) 250.0, paint);
            
            String dimensions = Integer.toString(canvas.getWidth());
            dimensions += "x" + Integer.toString(canvas.getHeight());
            canvas.drawText(dimensions.toString(), 0, dimensions.length(), (float) 200.0, (float) 200.0, paint);
            
            canvas.drawBitmap(character, (float) misty.x, (float) canvasHeight - 192 - misty.y, null);
        }
        
        public int[] getBoundingBox(int[] pos, int size) {
            int x, y, w, h, half_width, half_height;
            x = pos[0] - size;
            x = x > 0 ? x : 0;

            y = pos[1] - size;
            y = y > 0 ? y : 0;

            w = (pos[0] + size) - x;
            h = (pos[1] + size) - y;

            half_width  = w != 0 ? w / 2 : 0;
            half_height = h != 0 ? h / 2 : 0;

            int[] result = {
                x, y, w, h, half_width, half_height
            };
            return result;
        }
    
        
        public boolean Collision_Box_In_Box (int[] b1, int[] b2) {
            int left1, left2;
            int right1, right2;
            int top1, top2;
            int bottom1, bottom2;
                
            left1 = b1[0];
            left2 = b2[0];
            right1 = b1[0] + b1[2];
            right2 = b2[0] + b2[2];
            top1 = b1[1];
            top2 = b2[1];
            bottom1 = b1[1] + b1[3];
            bottom2 = b2[1] + b2[3];

            if (bottom1 < top2) return false;
            if (top1 > bottom2) return false;

            if (right1 < left2) return false;
            if (left1 > right2) return false;

            return true;

        }
        
        public int[] getTilePosition (int[] pos) {
            int x = Math.round((pos[0] - 16) / 32);
            int y = Math.round((pos[1] - 16) / 32);
            
            int[] result = {
                x, y
            };
            return result;
        }
        
        
        
        public Block getTile (int[] pos) {
            int[] tpos = getTilePosition(pos);
            int x = tpos[0];
            int y = tpos[1];
            
            int[] b1 = getBoundingBox(pos, 32);
            int[] b2 = getBoundingBox(pos, 32);
            
            Block tile = new Grass();
            if(x < map.length) {
                if(y < map[x].length) {
                    Block block = (Block) map[x][y];
                }
            }
            
            return tile;
        }
        
        public boolean isBlocked(int[] pos) {
            /*
             _____    __________________
            |_|_|_|  |-1,_-1|0,_-1|1,_-1|
            |_|_|_|  |-1,_ 0|0,_ 0|1,_ 0|
            |_|_|_|  |-1,_ 1|0,_ 1|1,_ 1|
            
            */
            pos = getTilePosition(pos);
            int x = pos[0] * 32;
            int y = pos[1] * 32;
            
            int[] topLeft   = {x - 32,y - 32};
            int[] topCenter = {x,     y - 32};
            int[] topRight  = {x + 32,y - 32};
            
            int[] centerLeft   = {x - 32,y};
            int[] centerCenter = {x     ,y};
            int[] centerRight  = {x + 32,y};
            
            int[] bb1 = getBoundingBox(topLeft, 16);
            int[] bb2 = getBoundingBox(topCenter, 16);
            int[] bb3 = getBoundingBox(topRight, 16);
            
            int[] bb4 = getBoundingBox(centerLeft, 16);
            int[] bb5 = getBoundingBox(centerCenter, 16);
            int[] bb6 = getBoundingBox(centerRight, 16);
            
            int[] bb7_pos = {x - 32,y + 32};
            int[] bb7 = getBoundingBox(new int[] {x - 32,y + 32}, 16);
            int[] bb8 = getBoundingBox(new int[] {x - 0,y + 32}, 16);
            int[] bb9 = getBoundingBox(new int[] {x + 32,y + 32}, 16);
            
            //-- I have to reduce the mobs' size by one or else it touches 
            // everything.
            int[] bb0 = getBoundingBox(pos, 15);
            int[][] hits = new int[9][6];
            int hitsSize = 0;
            
            if(Collision_Box_In_Box(bb0, bb1)) {
                hits[hitsSize] = bb1;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb2)) {
                hits[hitsSize] = bb2;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb3)) {
                hits[hitsSize] = bb3;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb4)) {
                hits[hitsSize] = bb4;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb5)) {
                hits[hitsSize] = bb5;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb6)) {
                hits[hitsSize] = bb6;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb7)) {
                hits[hitsSize] = bb7;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb8)) {
                hits[hitsSize] = bb8;
                hitsSize++;
            }
            if(Collision_Box_In_Box(bb0, bb9)) {
                hits[hitsSize] = bb9;
                hitsSize++;
            }
            
            for(int i = 0; i <= hitsSize; i++) {
                int[] hit = hits[i];
                int[] hitPos = {hit[0], hit[1]};
                int[] tilePos = getTilePosition(hitPos);
                
                Block tile = getTile(hitPos);
                if(tile.blocking) {
                    return true;
                }
            }
            
            return false;
        }
        
        public boolean checkPointInBox(float pointX, float pointY, int boxStartX, int boxStartY, int boxEndX, int boxEndY)
        {
            int Xmin, Xmax, Ymin, Ymax;
            boolean answer = false;

            // Find Min and Maxs
            Xmin = boxStartX; //Math.min(boxStartX, boxEndX);
            Xmax = boxEndX; //Math.max(boxStartX, boxEndX);
            Ymin = boxStartY; //Math.min(boxStartY, boxEndY);
            Ymax = boxEndY; //Math.max(boxStartY, boxEndY);

            // Now compare points X to boxes Xmax and Xmin
            if(pointX >= Xmin && pointX <= Xmax) {
                if(pointY >= Ymin && pointY <= Ymax) {
                    answer = true;
                }
            }
            
            return answer;
        }
        
        @Override
        public boolean onTouchEvent(android.view.MotionEvent event) {
            _x = (int) event.getX();
            _y = (int) event.getY();
            log = new StringBuilder("");
            
            for (int i = 0; i < event.getPointerCount(); i++) {
                /*
                log.append("#" ).append(i);
                log.append("(pid " ).append(event.getPointerId(i));
                log.append(")=" ).append((int) event.getX(i));
                log.append("," ).append((int) event.getY(i));
                if (i + 1 < event.getPointerCount()) {
                    log.append(";");
                }
                */
                
                // Left
                if(checkPointInBox(event.getX(i), event.getY(i), 10, 200, 55, 251)) {
                    log.append(" left");
                    direction = "left";
                }
                
                // Right
                else if(checkPointInBox(event.getX(i), event.getY(i), 80, 204, 138, 248)) {
                    log.append(" right");
                    direction = "right";
                }
                
                // Up
                else if(checkPointInBox(event.getX(i), event.getY(i), 52, 160, 95, 200)) {
                    log.append(" up");
                    direction = "up";
                }
                
                // Down
                else if(checkPointInBox(event.getX(i), event.getY(i), 52, 255, 95, 291)) {
                    log.append(" down");
                    direction = "down";
                }
                
                // Button A
                else if(checkPointInBox(event.getX(i), event.getY(i), 414, 207, 467, 260)) {
                    log.append(" button-a");
                    button = "a";
                }
                
                // Button B
                else if(checkPointInBox(event.getX(i), event.getY(i), 355, 235, 320, 300)) {
                    log.append(" button-b");
                    button = "b";
                }
            }
            
            if(event.getAction() == android.view.MotionEvent.ACTION_UP) {
                direction = "";
                button = "";
                log = new StringBuilder("");
            }
            
            //-- Vibrator???
        	// android.os.Vibrator v = (android.os.Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        	// v.vibrate(10);
            return true;
        }
 
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
 
        }
 
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            _thread.setRunning(true);
            _thread.start();
        }
 
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
    }
 
    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
 
        public TutorialThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
 
        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}