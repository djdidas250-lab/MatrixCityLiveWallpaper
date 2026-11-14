package c.m
import android.app.*;import android.content.*;import android.graphics.*;import android.os.Bundle;import android.widget.*
class MainActivity:Activity(){
 override fun onCreate(b:Bundle?){
  super.onCreate(b)
  val p=getSharedPreferences("m",0)
  val sv=ScrollView(this).apply{setBackgroundColor(Color.BLACK)}
  val r=LinearLayout(this).apply{orientation=1;setPadding(56,90,56,64)}
  fun l(s:String)=TextView(this).apply{text=s;setTextColor(Color.GREEN);textSize=16f;typeface=Typeface.MONOSPACE;setPadding(0,22,0,8)}
  val ths=arrayOf("MATRIX GREEN","CYBER CYAN","CRIMSON RED","SOLAR GOLD","RAINBOW PRISM")
  val bTh=Button(this).apply{text="THEME: ${ths[p.getInt("th",0)%5]}";setBackgroundColor(Color.DKGRAY);setTextColor(Color.GREEN);setOnClickListener{val n=(p.getInt("th",0)+1)%5;p.edit().putInt("th",n).apply();text="THEME: ${ths[n]}"}}
  val t1=l("SPEED: ${p.getInt("sp",12)}")
  val s1=SeekBar(this).apply{max=28;progress=p.getInt("sp",12)-2;setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{override fun onProgressChanged(s:SeekBar?,v:Int,f:Boolean){t1.text="SPEED: ${v+2}";p.edit().putInt("sp",v+2).apply()};override fun onStartTrackingTouch(s:SeekBar?){};override fun onStopTrackingTouch(s:SeekBar?){}})}
  val t2=l("SCALE: ${p.getInt("sz",42)}")
  val s2=SeekBar(this).apply{max=48;progress=p.getInt("sz",42)-20;setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{override fun onProgressChanged(s:SeekBar?,v:Int,f:Boolean){t2.text="SCALE: ${v+20}";p.edit().putInt("sz",v+20).apply()};override fun onStartTrackingTouch(s:SeekBar?){};override fun onStopTrackingTouch(s:SeekBar?){}})}
  fun sw(lb:String,k:String)=Switch(this).apply{text=lb;isChecked=p.getBoolean(k,true);setTextColor(Color.WHITE);typeface=Typeface.MONOSPACE;setPadding(0,18,0,18);setOnCheckedChangeListener{_,v->p.edit().putBoolean(k,v).apply()}}
  val btn=Button(this).apply{text="SET 3D WALLPAPER";setBackgroundColor(Color.GREEN);setTextColor(Color.BLACK);setOnClickListener{try{startActivity(Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,ComponentName(this@MainActivity,W::class.java)))}catch(e:Exception){startActivity(Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER))}}}
  r.addView(l("MATRIX 3D v7 SUITE"));r.addView(bTh);r.addView(t1);r.addView(s1);r.addView(t2);r.addView(s2);r.addView(sw("LENS BLOOM & BOKEH","f1"));r.addView(sw("TOUCH 3D REPEL & SURGE","f2"));r.addView(sw("FOLD-ADAPTIVE FOV","f3"));r.addView(sw("3D HORIZON GRID","f4"));r.addView(btn);sv.addView(r);setContentView(sv)
 }
}
