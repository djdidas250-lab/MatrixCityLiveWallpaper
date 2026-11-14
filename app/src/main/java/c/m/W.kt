package c.m
import android.content.Context
import android.graphics.*
import android.hardware.*
import android.os.*
import android.service.wallpaper.WallpaperService
import android.view.*
import kotlin.math.*
import kotlin.random.Random

class Col(var x: Float, var z: Float, var y: Float, var sp: Float, var ln: Int, var sg: Float, val gl: IntArray)

class W : WallpaperService() {
    override fun onCreateEngine() = E()
    inner class E : Engine(), SensorEventListener {
        val h = Handler(Looper.getMainLooper())
        var vis = false; var w = 0; var ht = 0; var bSz = 42f; var ml = 1.2f; var th = -1
        var f1 = true; var f2 = true; var f3 = true; var f4 = true
        var rx = -1f; var ry = -1f; var rr = 0f; var yw = 0f; var pt = 0f; var lt = 0L
        val sm by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
        val ch = "ｦｱｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ0123456789"
        var cols = ArrayList<Col>(); var spG = ArrayList<Bitmap>(); var spW = ArrayList<Bitmap>()
        val p = Paint(Paint.FILTER_BITMAP_FLAG)
        val pN = Paint(1).apply { strokeWidth = 3f; style = Paint.Style.STROKE }
        val pH = Paint(1)
        val rn = object : Runnable { override fun run() { dw(); if (vis) h.postDelayed(this, 16L) } }

        fun tc(i: Int) = intArrayOf(
            Color.rgb(0, 255, 75), Color.rgb(0, 240, 255), Color.rgb(255, 40, 75),
            Color.rgb(255, 205, 0), Color.HSVToColor(floatArrayOf((i * 23f) % 360f, 1f, 1f))
        )[th % 5]

        fun cfg() {
            val pr = getSharedPreferences("m", 0)
            bSz = pr.getInt("sz", 42).toFloat(); ml = pr.getInt("sp", 12) / 10f
            f1 = pr.getBoolean("f1", true); f2 = pr.getBoolean("f2", true)
            f3 = pr.getBoolean("f3", true); f4 = pr.getBoolean("f4", true)
            val nt = pr.getInt("th", 0)
            if (nt != th) { th = nt; mkSprites() }
        }

        fun mkSprites() {
            spG.clear(); spW.clear()
            val tp = Paint(1).apply { typeface = Typeface.MONOSPACE; isFakeBoldText = true; textSize = 54f; textAlign = Paint.Align.CENTER }
            for (i in ch.indices) {
                val g = ch[i].toString(); val base = tc(i)
                val r = Color.red(base); val gr = Color.green(base); val b = Color.blue(base)
                for (isW in 0..1) {
                    val bm = Bitmap.createBitmap(76, 76, Bitmap.Config.ARGB_8888); val cv = Canvas(bm)
                    for (d in 8 downTo 1) {
                        val k = 0.22f + d * 0.05f
                        tp.color = if (isW == 1) Color.rgb((60 + r * k).toInt().coerceAtMost(255), (60 + gr * k).toInt().coerceAtMost(255), (60 + b * k).toInt().coerceAtMost(255))
                        else Color.rgb((r * k * 0.65f).toInt(), (gr * k * 0.65f).toInt(), (b * k * 0.65f).toInt())
                        cv.drawText(g, 38f + d * 1.3f, 56f + d * 1.3f, tp)
                    }
                    tp.color = if (isW == 1) Color.WHITE else base
                    cv.drawText(g, 38f, 56f, tp)
                    if (isW == 1) spW.add(bm) else spG.add(bm)
                }
            }
        }

        fun init() {
            cols.clear()
            for (i in 0..74) {
                val a = (i / 75f) * 18.8495f + Random.nextFloat() * 0.3f
                val rad = 0.35f + (i / 75f) * 1.35f
                cols.add(Col(cos(a) * rad, sin(a) * rad, Random.nextFloat() * -50f, 0.28f + Random.nextFloat() * 0.25f, Random.nextInt(8, 18), 0f, IntArray(90) { Random.nextInt(ch.length) }))
            }
        }

        override fun onSensorChanged(e: SensorEvent) {
            if (e.sensor.type == Sensor.TYPE_GYROSCOPE) {
                if (lt != 0L) {
                    val dt = (e.timestamp - lt) * 1e-9f
                    yw += e.values.getOrElse(1) { 0f } * dt * 1.9f
                    pt = (pt + e.values.getOrElse(0) { 0f } * dt * 1.5f).coerceIn(-0.85f, 0.85f)
                }
                lt = e.timestamp
            } else if (e.sensor.type == Sensor.TYPE_ACCELEROMETER) { yw += -e.values.getOrElse(0) { 0f } * 0.004f }
        }

        override fun onAccuracyChanged(s: Sensor?, a: Int) {}
        override fun onTouchEvent(e: MotionEvent) { if (e.action == 0 || e.action == 2) { rx = e.x; ry = e.y; if (e.action == 0) rr = 12f }; super.onTouchEvent(e) }
        override fun onSurfaceChanged(hl: SurfaceHolder, f: Int, w: Int, ht: Int) { super.onSurfaceChanged(hl, f, w, ht); this.w = w; this.ht = ht; cfg(); if (cols.isEmpty()) init() }
        override fun onVisibilityChanged(v: Boolean) {
            super.onVisibilityChanged(v); vis = v; lt = 0L
            if (v) { cfg(); val gy = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE); if (gy != null) sm.registerListener(this, gy, 1) else sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { sm.registerListener(this, it, 1) }; h.post(rn) }
            else { sm.unregisterListener(this); h.removeCallbacks(rn) }
        }
        override fun onSurfaceDestroyed(hl: SurfaceHolder) { super.onSurfaceDestroyed(hl); vis = false; sm.unregisterListener(this); h.removeCallbacks(rn) }

        fun dw() {
            if (spG.isEmpty()) return
            val c = if (Build.VERSION.SDK_INT >= 23) surfaceHolder.lockHardwareCanvas() else surfaceHolder.lockCanvas()
            c ?: return
            try {
                c.drawColor(Color.BLACK); yw += 0.003f; pt *= 0.96f
                val cy = cos(yw); val sy = sin(yw); val hw = w * 0.5f; val hh = ht * 0.5f
                val fold = if (f3 && w > ht * 0.65f) 0.78f else 1f; val fov = 1.35f * fold; val accent = tc(0)
                if (f4) {
                    pN.color = accent; pN.alpha = 42; val gy = ht * 0.84f + pt * ht * 0.18f
                    c.drawLine(0f, gy, w.toFloat(), gy, pN)
                    for (g in -6..6) { val gx = hw + g * w * 0.12f + sin(yw) * w * 0.15f; c.drawLine(hw + (gx - hw) * 0.35f, gy, gx, ht.toFloat(), pN) }
                }
                val dst = RectF(); val visL = ArrayList<Pair<Float, Col>>(75)
                for (s in cols) {
                    val pv = s.y.toInt(); s.y += s.sp * ml * (1f + s.sg); s.sg *= 0.92f; val cr = s.y.toInt()
                    if (cr != pv && cr in s.gl.indices) s.gl[cr] = Random.nextInt(ch.length)
                    val rz3 = s.x * sy + s.z * cy + 1.85f
                    if (rz3 > 0.25f) visL.add(rz3 to s)
                    if ((cr - s.ln) * bSz > ht * 1.4f) { s.y = Random.nextFloat() * -16f; s.ln = Random.nextInt(8, 18) }
                }
                visL.sortByDescending { it.first }
                for ((rz3, s) in visL) {
                    val rx3 = s.x * cy - s.z * sy; val sc = fov / rz3; val sz = (bSz * sc * fold).coerceIn(10f, 110f)
                    var sx = hw + rx3 * hw * sc
                    if (f2 && rr > 0f) { val dx = sx - rx; val d = abs(dx); if (d < 220f) { sx += (if (dx >= 0f) 1f else -1f) * (220f - d) * 0.32f; if (d < 70f) s.sg = 1.4f } }
                    val br = (1.4f / (0.35f + rz3)).coerceIn(0.18f, 1f); val cr = s.y.toInt()
                    for (k in 0..s.ln) {
                        val r = cr - k
                        if (r in s.gl.indices) {
                            val sy2 = hh + ((r * bSz - hh) * sc) + pt * hh * sc
                            if (sy2 > -sz && sy2 < ht + sz) {
                                var al = (255 * br * (if (k == 0) 1f else 1f - k.toFloat() / s.ln)).toInt().coerceIn(10, 255)
                                if (f1 && rz3 > 2.1f) al = (al * 0.55f).toInt()
                                if (f1 && k == 0 && rz3 < 1.15f) { pH.color = accent; pH.alpha = (95 * br).toInt(); c.drawCircle(sx, sy2 - sz * 0.15f, sz * 0.85f, pH) }
                                p.alpha = al; val hs = sz * 0.68f; dst.set(sx - hs, sy2 - hs, sx + hs, sy2 + hs)
                                c.drawBitmap(if (k == 0 || s.sg > 0.5f) spW[s.gl[r]] else spG[s.gl[r]], null, dst, p)
                            }
                        }
                    }
                }
                if (f2 && rr > 0f) { pN.color = accent; pN.alpha = (255 * (1f - rr / 600f)).toInt().coerceIn(0, 255); c.drawCircle(rx, ry, rr, pN); rr += 24f; if (rr > 600f) rr = 0f }
            } finally { surfaceHolder.unlockCanvasAndPost(c) }
        }
    }
}
