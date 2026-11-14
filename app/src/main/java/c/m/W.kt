package c.m
import android.content.Context;import android.graphics.*;import android.hardware.*;import android.os.*;import android.service.wallpaper.WallpaperService;import android.view.*;import kotlin.math.*;import kotlin.random.Random
class Col(var x:Float,var z:Float,var y:Float,var sp:Float,var ln:Int,var sg:Float,var ox:Float,var vx:Float,var rz:Float,val gl:IntArray)
class W:WallpaperService(){override fun onCreateEngine()=E();inner class E:Engine(),SensorEventListener,Choreographer.FrameCallback{
var vis=false;var w=0;var ht=0;var bSz=42f;var ml=1.2f;var dns=85;var th=-1;var inten=0.65f
var f1=true;var f2=true;var f3=true;var f4=true;var tx=-1f;var ty=-1f;var tDown=false;var wR=0f;var wX=-1f;var wY=-1f
@Volatile var gWy=0f;@Volatile var gWx=0f;@Volatile var gWz=0f
var yw=0f;var pt=0f;var rl=0f;var smWy=0f;var smWx=0f;var smWz=0f;var lf=0L
var sThread:HandlerThread?=null;var sHandler:Handler?=null
val sm by lazy{getSystemService(Context.SENSOR_SERVICE) as SensorManager}
val ch="ｦｱｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ0123456789"
var cols=ArrayList<Col>();var spG=ArrayList<Bitmap>();var spW=ArrayList<Bitmap>();var bBmp:Bitmap?=null
val p=Paint(Paint.FILTER_BITMAP_FLAG)
val pN=Paint(Paint.ANTI_ALIAS_FLAG).apply{strokeWidth=3.5f;style=Paint.Style.STROKE}
val dst=RectF()
fun tc(i:Int)=intArrayOf(Color.rgb(0,255,85),Color.rgb(0,240,255),Color.rgb(255,42,78),Color.rgb(255,205,0),Color.rgb(195,65,255),Color.HSVToColor(floatArrayOf((i*23f)%360f,0.95f,1f)))[th%6]
fun cfg(){val pr=getSharedPreferences("m",0);bSz=pr.getInt("sz",42).toFloat();ml=pr.getInt("sp",12)/10f;inten=pr.getInt("in",65)/100f;val nd=pr.getInt("dn",85);f1=pr.getBoolean("f1",true);f2=pr.getBoolean("f2",true);f3=pr.getBoolean("f3",true);f4=pr.getBoolean("f4",true);val nt=pr.getInt("th",0);if(nt!=th){th=nt;mkSp()};if(nd!=dns||cols.isEmpty()){dns=nd;init()}}
fun mkSp(){spG.clear();spW.clear();val ac=tc(0);val rA=Color.red(ac);val gA=Color.green(ac);val bA=Color.blue(ac)
val b=Bitmap.createBitmap(128,128,Bitmap.Config.ARGB_8888);Canvas(b).drawCircle(64f,64f,64f,Paint(1).apply{shader=RadialGradient(64f,64f,64f,intArrayOf(Color.argb(120,255,255,255),Color.argb(65,rA,gA,bA),Color.argb(18,rA,gA,bA),0),floatArrayOf(0f,0.28f,0.62f,1f),Shader.TileMode.CLAMP)});bBmp=b
val tp=Paint(129).apply{typeface=Typeface.MONOSPACE;isFakeBoldText=true;textSize=64f;textAlign=Paint.Align.CENTER}
for(i in ch.indices){val g=ch[i].toString();val bs=tc(i);val r=Color.red(bs);val gr=Color.green(bs);val bl=Color.blue(bs)
for(w1 in 0..1){val bm=Bitmap.createBitmap(92,92,Bitmap.Config.ARGB_8888);val cv=Canvas(bm)
for(d in 10 downTo 1){val k=0.14f+(1f-d/10f)*0.42f;tp.color=if(w1==1)Color.rgb((45+r*k).toInt().coerceAtMost(255),(45+gr*k).toInt().coerceAtMost(255),(45+bl*k).toInt().coerceAtMost(255))else Color.rgb((r*k*0.6f).toInt(),(gr*k*0.6f).toInt(),(bl*k*0.6f).toInt());cv.drawText(g,46f+d*1.4f,67f+d*1.4f,tp)}
tp.color=if(w1==1)Color.argb(210,255,255,255)else Color.argb(180,(r+70).coerceAtMost(255),(gr+70).coerceAtMost(255),(bl+70).coerceAtMost(255));cv.drawText(g,45.2f,66.2f,tp)
tp.color=if(w1==1)-1 else bs;cv.drawText(g,46f,67f,tp);if(w1==1)spW.add(bm)else spG.add(bm)}}}
fun init(){cols.clear();for(i in 0 until dns){val a=i*2.3999632f+Random.nextFloat()*0.15f;val rd=0.30f+sqrt((i+0.5f)/dns)*1.52f;cols.add(Col(cos(a)*rd,sin(a)*rd,Random.nextFloat()*-55f,0.26f+Random.nextFloat()*0.26f,Random.nextInt(10,22),0f,0f,0f,0f,IntArray(100){Random.nextInt(ch.length)}))}}
override fun onSensorChanged(e:SensorEvent){if(!f4)return;if(e.sensor.type==Sensor.TYPE_GYROSCOPE){gWy=e.values.getOrElse(1){0f};gWx=e.values.getOrElse(0){0f};gWz=e.values.getOrElse(2){0f}}else if(e.sensor.type==Sensor.TYPE_ACCELEROMETER){gWy=-e.values.getOrElse(0){0f}*0.15f}}
override fun onAccuracyChanged(s:Sensor?,a:Int){}
override fun onTouchEvent(e:MotionEvent){if(f2)when(e.actionMasked){0->{tx=e.x;ty=e.y;tDown=true;wX=e.x;wY=e.y;wR=18f};2->{tx=e.x;ty=e.y;tDown=true};1,3->tDown=false};super.onTouchEvent(e)}
override fun onCommand(a:String?,x:Int,y:Int,z:Int,ex:Bundle?,r:Boolean):Bundle?{if(f2&&a=="android.wallpaper.tap"){wX=x.toFloat();wY=y.toFloat();wR=20f};return super.onCommand(a,x,y,z,ex,r)}
override fun onSurfaceChanged(hl:SurfaceHolder,f:Int,w:Int,ht:Int){super.onSurfaceChanged(hl,f,w,ht);this.w=w;this.ht=ht;cfg()}
override fun onVisibilityChanged(v:Boolean){super.onVisibilityChanged(v);vis=v;lf=0L;gWy=0f;gWx=0f;gWz=0f;smWy=0f;smWx=0f;smWz=0f;if(v){cfg();val st=HandlerThread("gs").apply{start()};sThread=st;val sh=Handler(st.looper);sHandler=sh;try{val gy=sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);if(gy!=null)sm.registerListener(this,gy,SensorManager.SENSOR_DELAY_GAME,sh)else sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let{sm.registerListener(this,it,SensorManager.SENSOR_DELAY_GAME,sh)}}catch(_:Exception){};Choreographer.getInstance().postFrameCallback(this)}else{sm.unregisterListener(this);sThread?.quitSafely();sThread=null;sHandler=null;Choreographer.getInstance().removeFrameCallback(this)}}
override fun onSurfaceDestroyed(hl:SurfaceHolder){super.onSurfaceDestroyed(hl);vis=false;sm.unregisterListener(this);sThread?.quitSafely();sThread=null;sHandler=null;Choreographer.getInstance().removeFrameCallback(this)}
override fun doFrame(frameTimeNanos:Long){if(!vis)return;dw(frameTimeNanos);Choreographer.getInstance().postFrameCallback(this)}
fun dw(now:Long){if(spG.isEmpty()||w<=0||ht<=0)return;val dt=if(lf==0L)0.016667f else((now-lf)*1e-9f).coerceIn(0.004f,0.033f);lf=now;val st=dt*60f
val c=if(Build.VERSION.SDK_INT>=23)surfaceHolder.lockHardwareCanvas()else surfaceHolder.lockCanvas();c?:return
try{c.drawColor(-16777216);val gs=0.42f+inten*0.72f;val fLp=1f-0.78f.pow(st)
smWy+=(gWy-smWy)*fLp;smWx+=(gWx-smWx)*fLp;smWz+=(gWz-smWz)*fLp
yw+=(0.0028f*(0.35f+inten*0.75f)*st)+(smWy*dt*1.55f*gs)
pt=(pt+(smWx*dt*1.15f*gs)).coerceIn(-0.75f,0.75f);pt*=0.975f.pow(st)
rl=(rl+(smWz*dt*0.42f*gs)).coerceIn(-0.28f,0.28f);rl*=0.97f.pow(st)
val cy=cos(yw);val sy=sin(yw);val hw=w*0.5f;val hh=ht*0.5f;val fd=if(f3&&w>ht*0.65f)0.78f else 1f;val fov=1.40f*fd;val ac=tc(0)
if(f4&&abs(rl)>0.001f){c.save();c.rotate(Math.toDegrees(rl.toDouble()).toFloat(),hw,hh)}
val spK=0.075f*st;val spD=0.85f.pow(st);val n=cols.size
for(i in 0 until n){val s=cols[i];val pv=s.y.toInt();s.y+=s.sp*ml*(1f+s.sg)*st;s.sg*=0.92f.pow(st);s.vx+=-s.ox*spK;s.vx*=spD;s.ox+=s.vx*st;val cr=s.y.toInt();if(cr!=pv&&cr in s.gl.indices)s.gl[cr]=Random.nextInt(ch.length);if(Random.nextFloat()<0.05f*st)s.gl[Random.nextInt(s.gl.size)]=Random.nextInt(ch.length);s.rz=s.x*sy+s.z*cy+1.88f;if((cr-s.ln)*bSz>ht*1.42f){s.y=Random.nextFloat()*-18f;s.ln=Random.nextInt(10,22)}}
for(i in 1 until n){val key=cols[i];var j=i-1;while(j>=0&&cols[j].rz<key.rz){cols[j+1]=cols[j];j--};cols[j+1]=key}
val tR=min(w,ht)*0.32f;val tR2=tR*tR
for(i in 0 until n){val s=cols[i];val rz3=s.rz;if(rz3<=0.24f)continue;val rx3=s.x*cy-s.z*sy;val sc=fov/rz3;val sz=(bSz*sc*fd).coerceIn(10f,118f);val bX=hw+rx3*hw*sc
if(f2){if(tDown){val dx=bX+s.ox-tx;if(abs(dx)<tR){val f=1f-abs(dx)/tR;s.vx+=(if(dx>=0f)1f else -1f)*f*f*2.4f*inten*st;s.sg=max(s.sg,f*1.35f*inten)}};if(wR>0f){val rD=abs(wR-abs(bX-wX));if(rD<95f){val at=(1f-wR/900f)*(1f-rD/95f);s.vx+=(if(bX>=wX)1f else -1f)*3.8f*at*inten*st;s.sg=max(s.sg,1.45f*at*inten)}}}
val br=(1.48f/(0.35f+rz3)).coerceIn(0.15f,1f);val cr=s.y.toInt();val hs=sz*0.70f
for(k in 0..s.ln){val r=cr-k;if(r in s.gl.indices){var sx=bX+s.ox;var sy2=hh+((r*bSz-hh)*sc)+pt*hh*sc
if(f2&&tDown){val dx=sx-tx;val dy=sy2-ty;val d2=dx*dx+dy*dy;if(d2<tR2&&d2>1f){val d=sqrt(d2);val rt=1f-d/tR;val ps=rt*rt*tR*0.58f*inten;sx+=(dx/d)*ps-(dy/d)*ps*0.14f;sy2+=(dy/d)*ps*0.68f+(dx/d)*ps*0.14f}}
if(sy2>-sz&&sy2<ht+sz){var al=(255*br*(if(k==0)1f else 1f-k.toFloat()/s.ln)).toInt().coerceIn(8,255);if(f1&&rz3>2.0f)al=(al*(1f-((rz3-2.0f)*0.45f).coerceIn(0f,0.65f))).toInt()
if(f1&&k==0&&rz3<1.35f)bBmp?.let{bb->p.alpha=(175*br*inten).toInt().coerceIn(0,220);val bR=sz*(1.15f+0.45f*inten);dst.set(sx-bR,sy2-sz*0.18f-bR,sx+bR,sy2-sz*0.18f+bR);c.drawBitmap(bb,null,dst,p)}
p.alpha=al;dst.set(sx-hs,sy2-hs,sx+hs,sy2+hs);c.drawBitmap(if(k==0||s.sg>0.45f)spW[s.gl[r]]else spG[s.gl[r]],null,dst,p)}}}}
if(f2&&wR>0f){pN.color=ac;pN.alpha=(220*(1f-wR/900f)*inten).toInt().coerceIn(0,255);c.drawCircle(wX,wY,wR,pN);wR+=34f*st;if(wR>900f)wR=0f}
if(f4&&abs(rl)>0.001f)c.restore()}finally{surfaceHolder.unlockCanvasAndPost(c)}}}}
