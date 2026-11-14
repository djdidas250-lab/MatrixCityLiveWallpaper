package c.m
import android.content.Context;import android.graphics.*;import android.hardware.*;import android.os.*;import android.service.wallpaper.WallpaperService;import android.view.*;import kotlin.math.*;import kotlin.random.Random
class W:WallpaperService(){override fun onCreateEngine()=E();inner class E:Engine(),SensorEventListener,Choreographer.FrameCallback{
var vis=false;var w=0;var ht=0;var bSz=42f;var ml=1.2f;var dns=85;var th=-1;var inten=0.65f;var blm=0.35f
var f1=true;var f2=true;var f3=true;var f4=true;var f5=true;var tx=-1f;var ty=-1f;var tDown=false;var wR=0f;var wX=-1f;var wY=-1f
@Volatile var rYw=0f;@Volatile var rPt=0f;@Volatile var rRl=0f;@Volatile var hasRot=false
var baseYw=0f;var autoYw=0f;var yw=0f;var pt=0f;var rl=0f;var lf=0L;var flyT=0f
val rotM=FloatArray(9);val ori=FloatArray(3);var sThread:HandlerThread?=null;var sHandler:Handler?=null
val sm by lazy{getSystemService(Context.SENSOR_SERVICE) as SensorManager}
var cols=ArrayList<Col>();val flies=Array(6){Fly(0f,0f,0f,0f,0,false)}
var spG=ArrayList<Bitmap>();var spW=ArrayList<Bitmap>();var bBmp:Bitmap?=null
val p=Paint(3);val pN=Paint(1).apply{strokeWidth=3.5f;style=Paint.Style.STROKE};val dst=RectF()
fun cfg(){val pr=getSharedPreferences("m",0);bSz=pr.getInt("sz",42).toFloat();ml=pr.getInt("sp",12)/10f;inten=pr.getInt("in",65)/100f;blm=pr.getInt("bl",35)/100f;val nd=pr.getInt("dn",85);f1=pr.getBoolean("f1",true);f2=pr.getBoolean("f2",true);f3=pr.getBoolean("f3",true);f4=pr.getBoolean("f4",true);f5=pr.getBoolean("f5",true);val nt=pr.getInt("th",0);if(nt!=th){th=nt;bBmp=M.mkBloom(M.tc(th,0));M.mkSprites(th,spG,spW)};if(nd!=dns||cols.isEmpty()){dns=nd;init()}}
fun init(){cols.clear();for(i in 0 until dns){val a=i*2.3999632f+Random.nextFloat()*0.15f;val rd=0.30f+sqrt((i+0.5f)/dns)*1.52f;cols.add(Col(cos(a)*rd,sin(a)*rd,Random.nextFloat()*-55f,0.26f+Random.nextFloat()*0.26f,Random.nextInt(10,22),0f,0f,0f,0f,IntArray(100){Random.nextInt(M.CH.length)}))}}
override fun onSensorChanged(e:SensorEvent){if(!f4)return;if(e.sensor.type==Sensor.TYPE_GAME_ROTATION_VECTOR||e.sensor.type==Sensor.TYPE_ROTATION_VECTOR){SensorManager.getRotationMatrixFromVector(rotM,e.values);SensorManager.getOrientation(rotM,ori);val az=ori.getOrElse(0){0f};val pitch=ori.getOrElse(1){-0.75f};val roll=ori.getOrElse(2){0f};if(!hasRot){baseYw=az;hasRot=true};rYw=az-baseYw;rPt=((pitch+0.75f)*0.55f).coerceIn(-0.45f,0.45f);rRl=(roll*0.32f).coerceIn(-0.22f,0.22f)}else if(e.sensor.type==Sensor.TYPE_ACCELEROMETER){val gx=e.values.getOrElse(0){0f};val gy=e.values.getOrElse(1){6.5f};rYw+=-gx*0.0025f;rPt=((gy-6.5f)*0.065f).coerceIn(-0.45f,0.45f);rRl=(-gx*0.02f).coerceIn(-0.2f,0.2f)}}
override fun onAccuracyChanged(s:Sensor?,a:Int){}
override fun onTouchEvent(e:MotionEvent){if(f2)when(e.actionMasked){0->{tx=e.x;ty=e.y;tDown=true;wX=e.x;wY=e.y;wR=18f};2->{tx=e.x;ty=e.y;tDown=true};1,3->tDown=false};super.onTouchEvent(e)}
override fun onCommand(a:String?,x:Int,y:Int,z:Int,ex:Bundle?,r:Boolean):Bundle?{if(f2&&a=="android.wallpaper.tap"){wX=x.toFloat();wY=y.toFloat();wR=20f};return super.onCommand(a,x,y,z,ex,r)}
override fun onSurfaceChanged(hl:SurfaceHolder,f:Int,w:Int,ht:Int){super.onSurfaceChanged(hl,f,w,ht);this.w=w;this.ht=ht;cfg()}
override fun onVisibilityChanged(v:Boolean){super.onVisibilityChanged(v);vis=v;lf=0L;hasRot=false;if(v){cfg();val st=HandlerThread("gs").apply{start()};sThread=st;val sh=Handler(st.looper);sHandler=sh;try{val rv=sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)?:sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?:sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);rv?.let{sm.registerListener(this,it,SensorManager.SENSOR_DELAY_GAME,sh)}}catch(_:Exception){};Choreographer.getInstance().postFrameCallback(this)}else{sm.unregisterListener(this);sThread?.quitSafely();sThread=null;sHandler=null;Choreographer.getInstance().removeFrameCallback(this)}}
override fun onSurfaceDestroyed(hl:SurfaceHolder){super.onSurfaceDestroyed(hl);vis=false;sm.unregisterListener(this);sThread?.quitSafely();sThread=null;sHandler=null;Choreographer.getInstance().removeFrameCallback(this)}
override fun doFrame(frameTimeNanos:Long){if(!vis)return;dw(frameTimeNanos);Choreographer.getInstance().postFrameCallback(this)}
fun dw(now:Long){if(spG.isEmpty()||w<=0||ht<=0)return;val dt=if(lf==0L)0.016667f else((now-lf)*1e-9f).coerceIn(0.004f,0.033f);lf=now;val st=dt*60f
val c=if(Build.VERSION.SDK_INT>=23)surfaceHolder.lockHardwareCanvas()else surfaceHolder.lockCanvas();c?:return
try{c.drawColor(-16777216);val gs=0.45f+inten*0.75f;val fLp=1f-0.84f.pow(st);autoYw+=0.0028f*(0.35f+inten*0.75f)*st;val tgtYw=autoYw+(if(f4)rYw*1.35f*gs else 0f);var diff=tgtYw-yw;while(diff>3.14159f)diff-=6.28318f;while(diff<-3.14159f)diff+=6.28318f;yw+=diff*fLp;pt+=((if(f4)rPt*gs else 0f)-pt)*fLp;rl+=((if(f4)rRl*gs else 0f)-rl)*fLp
val cy=cos(yw);val sy=sin(yw);val hw=w*0.5f;val hh=ht*0.5f;val fd=if(f3&&w>ht*0.65f)0.78f else 1f;val fov=1.40f*fd;val ac=M.tc(th,0)
if(f4&&abs(rl)>0.001f){c.save();c.rotate(Math.toDegrees(rl.toDouble()).toFloat(),hw,hh)}
val spK=0.075f*st;val spD=0.85f.pow(st);val n=cols.size
for(i in 0 until n){val s=cols[i];val pv=s.y.toInt();s.y+=s.sp*ml*(1f+s.sg)*st;s.sg*=0.92f.pow(st);s.vx+=-s.ox*spK;s.vx*=spD;s.ox+=s.vx*st;val cr=s.y.toInt();if(cr!=pv&&cr in s.gl.indices)s.gl[cr]=Random.nextInt(M.CH.length);if(Random.nextFloat()<0.05f*st)s.gl[Random.nextInt(s.gl.size)]=Random.nextInt(M.CH.length);s.rz=s.x*sy+s.z*cy+1.88f;if((cr-s.ln)*bSz>ht*1.55f){s.y=Random.nextFloat()*-22f;s.ln=Random.nextInt(10,22)}}
for(i in 1 until n){val key=cols[i];var j=i-1;while(j>=0&&cols[j].rz<key.rz){cols[j+1]=cols[j];j--};cols[j+1]=key}
val tR=min(w,ht)*0.32f;val tR2=tR*tR
for(i in 0 until n){val s=cols[i];val rz3=s.rz;if(rz3<=0.24f)continue;val rx3=s.x*cy-s.z*sy;val sc=fov/rz3;val sz=(bSz*sc*fd).coerceIn(10f,118f);val bX=hw+rx3*hw*sc
if(f2){if(tDown){val dx=bX+s.ox-tx;if(abs(dx)<tR){val f=1f-abs(dx)/tR;s.vx+=(if(dx>=0f)1f else -1f)*f*f*2.4f*inten*st;s.sg=max(s.sg,f*1.35f*inten)}};if(wR>0f){val rD=abs(wR-abs(bX-wX));if(rD<95f){val at=(1f-wR/900f)*(1f-rD/95f);s.vx+=(if(bX>=wX)1f else -1f)*3.8f*at*inten*st;s.sg=max(s.sg,1.45f*at*inten)}}}
val br=(1.48f/(0.35f+rz3)).coerceIn(0.15f,1f);val cr=s.y.toInt();val hs=sz*0.70f
for(k in 0..s.ln){val r=cr-k;if(r in s.gl.indices){var sx=bX+s.ox;var sy2=hh+((r*bSz-hh)*sc)+pt*hh*sc
if(f2&&tDown){val dx=sx-tx;val dy=sy2-ty;val d2=dx*dx+dy*dy;if(d2<tR2&&d2>1f){val d=sqrt(d2);val rt=1f-d/tR;val ps=rt*rt*tR*0.58f*inten;sx+=(dx/d)*ps-(dy/d)*ps*0.14f;sy2+=(dy/d)*ps*0.68f+(dx/d)*ps*0.14f}}
if(sy2>-sz*2.2f&&sy2<ht+sz*2.2f){var al=(255*br*(if(k==0)1f else 1f-k.toFloat()/s.ln)).toInt().coerceIn(8,255);if(f1&&rz3>1.9f)al=(al*(1f-((rz3-1.9f)*0.65f*blm).coerceIn(0f,0.85f))).toInt()
if(f1&&blm>0.01f&&k==0&&rz3<1.55f)bBmp?.let{bb->p.alpha=(255*br*blm).toInt().coerceIn(0,255);val bR=sz*(0.9f+1.1f*blm);dst.set(sx-bR,sy2-sz*0.18f-bR,sx+bR,sy2-sz*0.18f+bR);c.drawBitmap(bb,null,dst,p)}
p.alpha=al;dst.set(sx-hs,sy2-hs,sx+hs,sy2+hs);c.drawBitmap(if(k==0||s.sg>0.45f)spW[s.gl[r]]else spG[s.gl[r]],null,dst,p)}}}}
if(f5){flyT-=dt;if(flyT<=0f&&n>0){flyT=0.9f+Random.nextFloat()*1.4f;for(fl in flies)if(!fl.act){val s=cols[Random.nextInt(n)];val cr=s.y.toInt();if(s.rz>0.6f&&cr in s.gl.indices){val rx3=s.x*cy-s.z*sy;val sc=fov/s.rz;val sy2=hh+((cr*bSz-hh)*sc)+pt*hh*sc;if(sy2>ht*0.1f&&sy2<ht*0.9f){fl.x=rx3*sc*s.rz;fl.y=(sy2-hh)/(hh*sc)*s.rz;fl.z=s.rz;fl.vz=0.018f+Random.nextFloat()*0.012f;fl.gl=s.gl[cr];fl.act=true;break}}}}
for(fl in flies)if(fl.act){fl.z-=fl.vz*(0.65f+ml*0.4f)*st;if(fl.z<=0.14f){fl.act=false;continue};val sc=fov/fl.z;val sz=(bSz*sc*fd).coerceIn(14f,240f);val sx=hw+fl.x*hw*sc;val sy2=hh+fl.y*hh*sc+pt*hh*sc;val fade=if(fl.z<0.42f)((fl.z-0.14f)/0.28f).coerceIn(0f,1f)else 1f;if(f1&&blm>0.01f)bBmp?.let{bb->p.alpha=(210*fade*blm).toInt();val bR=sz*(1.0f+0.9f*blm);dst.set(sx-bR,sy2-sz*0.18f-bR,sx+bR,sy2-sz*0.18f+bR);c.drawBitmap(bb,null,dst,p)};p.alpha=(255*fade).toInt();val hs=sz*0.72f;dst.set(sx-hs,sy2-hs,sx+hs,sy2+hs);c.drawBitmap(spW[fl.gl],null,dst,p)}}
if(f2&&wR>0f){pN.color=ac;pN.alpha=(220*(1f-wR/900f)*inten).toInt().coerceIn(0,255);c.drawCircle(wX,wY,wR,pN);wR+=34f*st;if(wR>900f)wR=0f}
if(f4&&abs(rl)>0.001f)c.restore()}finally{surfaceHolder.unlockCanvasAndPost(c)}}}}
