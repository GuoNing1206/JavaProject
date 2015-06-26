package com.example.meshshow;

import android.content.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
//import java.nio.IntBuffer;

import android.graphics.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;

public class ClearRenderer implements Renderer {

	private Context context = null;
	private GL10 m_gl;
	
	private int m_TriCount = 0;
	private float[] m_MeshVertex = null;
	private float[] m_MeshTexCoord = null;
	private FloatBuffer m_VertexBuffer;
	private FloatBuffer m_TexCoordBuffer;
	
	private float m_AngleH = 0;
	private float m_AngleV = -70;
	private float m_SpeedH = 0;
	private float m_SpeedV = 0;
	private float m_Distance = 100.0f;
	private float m_AutoRotateStep = 0.5f;
	private float m_VertBias = 0.0f;
	private int m_TextureId = 0;
	private boolean m_LoadComplete = false;
	private int m_MeshId = 0;
	private int m_TexId = 0;

	public ClearRenderer(Context A_Context) {
		context = A_Context;
	}
	
	private void loadMesh(int A_MeshId) {
		try {
	      InputStream is = context.getResources().openRawResource(A_MeshId);
	      InputStreamReader inputreader = new InputStreamReader(is);
	      BufferedReader buffreader = new BufferedReader(inputreader);
	
	      String sTriCount = buffreader.readLine();
	      m_TriCount = Integer.parseInt(sTriCount);
	
	      m_MeshVertex = new float[m_TriCount * 3 * 3];
	      m_MeshTexCoord = new float[m_TriCount * 3 * 2];
	
	      for(int i = 0; i < m_TriCount * 3; i++) {
	        String sVertex = buffreader.readLine();
	        String vVertex[] = sVertex.split(",");
	        float x = Float.parseFloat(vVertex[0]);
	        float y = Float.parseFloat(vVertex[1]);
	        float z = Float.parseFloat(vVertex[2]);
	
	        m_MeshVertex[i*3] = x;
	        m_MeshVertex[i*3+1] = y;
	        m_MeshVertex[i*3+2] = z;
	      }
	
	      for(int i = 0; i < m_TriCount * 3; i++) {
	        String sVertex = buffreader.readLine();
	        String vVertex[] = sVertex.split(",");
	        float x = Float.parseFloat(vVertex[0]);
	        float y = Float.parseFloat(vVertex[1]);
	
	        m_MeshTexCoord[i*2] = x;
	        m_MeshTexCoord[i*2+1] = -y;
	      }
	
	      is.close();
	    }
	    catch(Exception e){
	    }
		
		m_VertexBuffer = floatBufferUtil(m_MeshVertex);
		m_TexCoordBuffer = floatBufferUtil(m_MeshTexCoord);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		m_gl = gl;
		
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL10.GL_GREATER, 0.1f);

		loadCharacter(R.raw.jiutianmesh, R.drawable.jiutian);
	}

	public void loadCharacter(int A_MeshId, int A_TextureId) {
		m_LoadComplete = false;
		m_MeshId = A_MeshId;
		m_TexId = A_TextureId;
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = (float)width / height;
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 1000);
	}

	public void setSpeed(float angleH, float angleV) {
		m_SpeedH = angleH;
		m_SpeedV = angleV;
		if(m_SpeedH > 0.0f)
			m_AutoRotateStep = Math.abs(m_AutoRotateStep);
		else
			m_AutoRotateStep = -Math.abs(m_AutoRotateStep);
	}
	
	public void setVerticalBias(float A_Bias) {
		m_VertBias += A_Bias;
		if(m_VertBias > 500.0f)
			m_VertBias = 500.0f;
		if(m_VertBias < -500.0f)
			m_VertBias = -500.0f;
	}
	
	public void zoom(float A_Factor) {
		m_Distance *= A_Factor;
		if(m_Distance > 1000.0f)
			m_Distance = 1000.0f;
		if(m_Distance < 10.0f)
			m_Distance = 10.0f;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		if(m_MeshId != 0 && m_TexId != 0) {
			loadMesh(m_MeshId);
			loadTexture(m_gl, m_TexId);
			m_LoadComplete = true;
			m_MeshId = 0;
			m_TexId = 0;
		}
		
		if(!m_LoadComplete)
			return;
		
		m_AngleH += m_SpeedH;
		m_AngleV += m_SpeedV;
		m_SpeedH /= 1.05f;
		m_SpeedV /= 1.05f;
		
		if(m_AngleV > -20.0f)
			m_AngleV = -20.0f;
		if(m_AngleV < -160.0f)
			m_AngleV = -160.0f;
		
		if(m_SpeedH < 0.0001f && m_SpeedH > -0.0001f)
			m_AngleH += m_AutoRotateStep;
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); 
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -m_Distance);
		gl.glRotatef(m_AngleV, 1, 0, 0);
		gl.glRotatef(m_AngleH, 0, 0, 1);
		gl.glTranslatef(0, 0, m_VertBias);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_VertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_TexCoordBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, m_TriCount * 3);
		
		gl.glFinish();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

/*	private IntBuffer intBufferUtil(int[] arr) {
		IntBuffer mBuffer;
		ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
		qbb.order(ByteOrder.nativeOrder());
		mBuffer = qbb.asIntBuffer();
		mBuffer.put(arr);
		mBuffer.position(0);
		return mBuffer;
	}*/
	
	private FloatBuffer floatBufferUtil(float[] arr) {
		FloatBuffer mBuffer;
		ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
		qbb.order(ByteOrder.nativeOrder());
		mBuffer = qbb.asFloatBuffer();
		mBuffer.put(arr);
		mBuffer.position(0);
		return mBuffer;
	}
	
	private void loadTexture(GL10 gl, int A_TextureId) {
		Bitmap bmp = null;
		try {
			bmp = BitmapFactory.decodeResource(context.getResources(), A_TextureId);
			int[] textures = new int[1];
			gl.glGenTextures(1, textures, 0);
			m_TextureId = textures[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_TextureId);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		}
		finally {
			if(bmp != null)
				bmp.recycle();
		}
	}
}











