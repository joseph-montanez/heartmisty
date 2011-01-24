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
 
    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private TutorialThread _thread;
        private int _x = 20;
        private int _y = 20;
        private int frames = 0;
        private String fps = "0";
        private long t = System.currentTimeMillis(); 
        private StringBuilder log = new StringBuilder("");
        private int[][] map = new int[10][5];
 
        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            
            map[0][0] = R.drawable.grass;
            map[0][1] = R.drawable.grass;
            map[0][2] = R.drawable.grass;
            map[0][3] = R.drawable.grass;
            map[0][4] = R.drawable.grass;
            
            map[1][0] = R.drawable.grass;
            map[1][1] = R.drawable.grass;
            map[1][2] = R.drawable.grass;
            map[1][3] = R.drawable.grass;
            map[1][4] = R.drawable.grass;
            
            map[2][0] = R.drawable.grass;
            map[2][1] = R.drawable.grass;
            map[2][2] = R.drawable.grass;
            map[2][3] = R.drawable.grass;
            map[2][4] = R.drawable.grass;
            
            map[3][0] = R.drawable.grass;
            map[3][1] = R.drawable.grass;
            map[3][2] = R.drawable.grass;
            map[3][3] = R.drawable.grass;
            map[3][4] = R.drawable.grass;
            
            map[4][0] = R.drawable.grass;
            map[4][1] = R.drawable.grass;
            map[4][2] = R.drawable.grass;
            map[4][3] = R.drawable.grass;
            map[4][4] = R.drawable.grass;
            
            map[5][0] = R.drawable.grass;
            map[5][1] = R.drawable.grass;
            map[5][2] = R.drawable.grass;
            map[5][3] = R.drawable.grass;
            map[5][4] = R.drawable.grass;
            
            map[6][0] = R.drawable.grass;
            map[6][1] = R.drawable.grass;
            map[6][2] = R.drawable.grass;
            map[6][3] = R.drawable.grass;
            map[6][4] = R.drawable.grass;
            
            map[7][0] = R.drawable.grass;
            map[7][1] = R.drawable.grass;
            map[7][2] = R.drawable.water;
            map[7][3] = R.drawable.grass;
            map[7][4] = R.drawable.grass;
            
            map[8][0] = R.drawable.grass;
            map[8][1] = R.drawable.water;
            map[8][2] = R.drawable.water;
            map[8][3] = R.drawable.water;
            map[8][4] = R.drawable.grass;
            
            map[9][0] = R.drawable.grass;
            map[9][1] = R.drawable.water;
            map[9][2] = R.drawable.water;
            map[9][3] = R.drawable.grass;
            map[9][4] = R.drawable.grass;
            
            _thread = new TutorialThread(getHolder(), this);
        }
 
        @Override
        public void onDraw(Canvas canvas) {
            Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
            Bitmap button = BitmapFactory.decodeResource(getResources(), R.drawable.button);
            Bitmap dpad = BitmapFactory.decodeResource(getResources(), R.drawable.d_pad);
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
            java.util.HashMap<Integer, Bitmap> tiles = new java.util.HashMap<Integer, Bitmap>();
            for(int x = 0; x < map.length; x++) {
                for(int y = 0; y < map[x].length; y++) {
                    int tileId = map[x][y];
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
            //canvas.drawText(log.toString(), 0, log.length(), (float) 200.0, (float) 200.0, paint);
            
            String dimensions = Integer.toString(canvas.getWidth());
            dimensions += "x" + Integer.toString(canvas.getHeight());
            canvas.drawText(dimensions.toString(), 0, dimensions.length(), (float) 200.0, (float) 200.0, paint);
        }
        
        @Override
        public boolean onTouchEvent(android.view.MotionEvent event) {
            _x = (int) event.getX();
            _y = (int) event.getY();
            log = new StringBuilder("");
            for (int i = 0; i < event.getPointerCount(); i++) {
                log.append("#" ).append(i);
                log.append("(pid " ).append(event.getPointerId(i));
                log.append(")=" ).append((int) event.getX(i));
                log.append("," ).append((int) event.getY(i));
                if (i + 1 < event.getPointerCount()) {
                    log.append(";" );
                }
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