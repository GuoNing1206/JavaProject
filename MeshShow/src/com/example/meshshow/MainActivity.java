package com.example.meshshow;

//import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import com.example.meshshow.*;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.os.Build;
import android.widget.Toast;

//import java.io.*;

public class MainActivity extends ActionBarActivity implements OnGestureListener {
	private GLSurfaceView mGLView = null;
	private ClearRenderer render = null;
	private GestureDetector dector = null;
	private float m_CurDistance = 0.0f;
	private float m_LastDistance = -1.0f;
	private float m_CurBiasV = 0.0f;
	private float m_LastBiasV = -1.0f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    mGLView = new GLSurfaceView(this); 
	    render = new ClearRenderer(this);
	    mGLView.setRenderer(render); 
	    setContentView(mGLView);
	    dector = new GestureDetector(this, this);
	    
	    this.setTitle("九天玄女");
	    
//		setContentView(R.layout.activity_main);
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_jiutian) {
			this.setTitle("九天玄女");
			Toast.makeText(this, "正在加载九天玄女...", Toast.LENGTH_SHORT).show();
			render.loadCharacter(R.raw.jiutianmesh, R.drawable.jiutian);
			return true;
		}
		if (id == R.id.action_shangren) {
			this.setTitle("商人");
			Toast.makeText(this, "正在加载商人...", Toast.LENGTH_SHORT).show();
			render.loadCharacter(R.raw.shangrenmesh, R.drawable.shangren);
			return true;
		}
		if (id == R.id.action_minzhong) {
			this.setTitle("武夫");
			Toast.makeText(this, "正在加载武夫...", Toast.LENGTH_SHORT).show();
			render.loadCharacter(R.raw.minzhong, R.drawable.minzhong);
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		
		if(me.getAction() == MotionEvent.ACTION_MOVE) {
			if (me.getPointerCount() >= 2) {
				float offsetX = me.getX(0) - me.getX(1);
				float offsetY = me.getY(0) - me.getY(1);
				m_CurDistance = (float) Math.sqrt(offsetX * offsetX
						+ offsetY * offsetY);
				
				if(m_LastDistance < 0)
					m_LastDistance = m_CurDistance;
				else {
					if (m_CurDistance - m_LastDistance > 5) {
						// Log.d(TAG, "放大！！！");
						render.zoom(0.98f);
						m_LastDistance = m_CurDistance;
					}
					else if (m_LastDistance - m_CurDistance > 5) {
						// Log.d(TAG, "缩小！！！");
						render.zoom(1.02f);
						m_LastDistance = m_CurDistance;
					}
				}
				
				m_CurBiasV = (me.getY(0) + me.getY(1)) * 0.5f;
				if(m_CurBiasV - m_LastBiasV < 50.0f && m_CurBiasV - m_LastBiasV > -50.0f) {
					render.setVerticalBias((m_LastBiasV - m_CurBiasV)*0.1f);
				}
				m_LastBiasV = m_CurBiasV;
			}
		}
		
		
		
		if(dector != null)
			return dector.onTouchEvent(me);
		return false;
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		render.setSpeed(-distanceX/3.0f, -distanceY/3.0f);
//		this.setTitle(String.format("%f : %f", distanceX, distanceY));
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		//render.setGesture(velocityX/600.0f, velocityY/600.0f);
		//this.setTitle(String.format("%f : %f", velocityX, velocityY));
		return true;
	}
	
}
