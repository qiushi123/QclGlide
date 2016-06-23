# QclGlide
Glide加载gif动图，Glide带加载动画（动画可以自定义）

#先看效果图
###上面是一个gif动图，下面是通过glide把图片设成圆形图片
![image](https://github.com/qiushi123/QclGlide/blob/master/images/1_meitu_1.png?raw=true)

一. Android-stduio引入类库
在build.gradle中添加依赖：
    compile 'com.github.bumptech.glide:glide:3.7.0'

需要support-v4库的支持，如果你的项目没有support-v4库(项目默认已经添加了)，还需要添加support-v4依赖：
如果你用的是3.0以后sdk下面的v4包就不用导入了
    compile 'com.android.support:support-v4:23.3.0'
然后配置混淆规则：
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }

其中第一个混淆规则表明不混淆所有的GlideModule。

二. 集成网络框架OkHttp

Glide的网络请求部分可以使用当前最流行的网络请求框架Volley或OkHttp，也可以通过Glide的ModelLoader接口自己写网络请求。 
Glide默认使用HttpUrlConnection进行网络请求，为了让APP保持一致的网络请求形式，可以让Glide使用我们指定的网络请求形式请求网络资源，
这里我们选OkHttp (具有支持HTTP/2、利用连接池技术减少请求延迟、缓存响应结果等等优点)，需要添加一个集成库：

    //OkHttp 2.x
    //compile 'com.github.bumptech.glide:okhttp-integration:1.4.0@aar'
    //compile 'com.squareup.okhttp:okhttp:2.7.5'

    //OkHttp 3.x
    compile 'com.github.bumptech.glide:okhttp3-integration:1.4.0@aar'
    compile 'com.squareup.okhttp3:okhttp:3.2.0'

注意： 
1. OkHttp 2.x和OkHttp 3.x需使用不同的集成库。 
2. Gradle会自动将OkHttpGlideModule合并到应用的manifest文件中。 
3. 如果你没有对所有的GlideModule配置混淆规则(即没有使用-keep public class * implements com.bumptech.glide.module.GlideModule)，
则需要把OkHttp的GlideModule进行防混淆配置：

-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule

三. 使用
简单使用：
        Glide
           .with(this)
           .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
           .into(imageView);

Glide.with()
	with(Context context). 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制。
	with(Activity activity).使用Activity作为上下文，Glide的请求会受到Activity生命周期控制。
	with(FragmentActivity activity).Glide的请求会受到FragmentActivity生命周期控制。
	with(android.app.Fragment fragment).Glide的请求会受到Fragment 生命周期控制。
	with(android.support.v4.app.Fragment fragment).Glide的请求会受到Fragment生命周期控制。
返回关联了相应上下文的RequestManager实例。

requestManager.load()

Glide基本可以load任何可以拿到的媒体资源，如： 
	load SD卡资源：load("file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg") 
	load assets资源：load("file:///android_asset/f003.gif") 
	load raw资源：load("Android.resource://com.frank.glide/raw/raw_1")或load("android.resource://com.frank.glide/raw/"+R.raw.raw_1) 
	load drawable资源：load("android.resource://com.frank.glide/drawable/news")或load("android.resource://com.frank.glide/drawable/"+R.drawable.news) 
	load ContentProvider资源：load("content://media/external/images/media/139469") 
	load http资源：load("http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg") 
	load https资源：load("https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp") 
当然，load不限于String类型，还可以： 
	load(Uri uri)，load(File file)，load(Integer resourceId)，load(URL url)，load(byte[] model)，
	load(T model)，loadFromMediaStore(Uri uri)。
	load的资源也可以是本地视频，如果想要load网络视频或更高级的操作可以使用VideoView等其它控件完成。 
	而且可以使用自己的ModelLoader进行资源加载： 
	using(ModelLoader<A, T> modelLoader, Class<T> dataClass)，using(final StreamModelLoader<T> modelLoader)，
	using(StreamByteArrayLoader modelLoader)，using(final FileDescriptorModelLoader<T> modelLoader)。
返回GenericRequestBuilder实例。

GenericRequestBuilder

GenericRequestBuilder<ModelType,DataType,ResourceType,TranscodeType>是最顶层的Request Builder，
用于处理选项设置和开始一般resource类型资源的加载。其中ModelType是指代表资源的类型，
如"http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg"这个String就代表了一张图片资源，
所以这个ModelType就是String。DataType是指ModelLoader提供的，可以被ResourceDecoder解码的数据类型。
ResourceType是指将要加载的resource类型。TranscodeType是指已解码的资源将要被转成的资源类型。

常用设置（比如动画，缓存等）
	thumbnail(float sizeMultiplier). 请求给定系数的缩略图。如果缩略图比全尺寸图先加载完，就显示缩略图，
			否则就不显示。系数sizeMultiplier必须在(0,1)之间，可以递归调用该方法。
	sizeMultiplier(float sizeMultiplier). 在加载资源之前给Target大小设置系数。
	diskCacheStrategy(DiskCacheStrategy strategy).设置缓存策略。
		DiskCacheStrategy.SOURCE：缓存原始数据，
		DiskCacheStrategy.RESULT：缓存变换后的资源数据，
		DiskCacheStrategy.NONE：什么都不缓存，
		DiskCacheStrategy.ALL：缓存SOURC和RESULT。
		默认采用DiskCacheStrategy.RESULT策略，对于download only操作要使用DiskCacheStrategy.SOURCE。
	priority(Priority priority). 指定加载的优先级，优先级越高越优先加载，
		但不保证所有图片都按序加载。枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW。默认为Priority.NORMAL。
	dontAnimate(). 移除所有的动画。
	animate(int animationId). 在异步加载资源完成时会执行该动画。
	animate(ViewPropertyAnimation.Animator animator). 在异步加载资源完成时会执行该动画。
	placeholder(int resourceId). 设置资源加载过程中的占位Drawable。
	placeholder(Drawable drawable). 设置资源加载过程中的占位Drawable。
	fallback(int resourceId). 设置model为空时要显示的Drawable。如果没设置fallback，model为空时将显示error的Drawable，
		如果error的Drawable也没设置，就显示placeholder的Drawable。
	fallback(Drawable drawable).设置model为空时显示的Drawable。
	error(int resourceId).设置load失败时显示的Drawable。
	error(Drawable drawable).设置load失败时显示的Drawable。
	listener(RequestListener<? super ModelType, TranscodeType> requestListener). 监听资源加载的请求状态，
		可以使用两个回调：onResourceReady(R resource, T model, Target<R> target, boolean isFromMemoryCache, 
		boolean isFirstResource)和onException(Exception e, T model, Target&lt;R&gt; target, 
		boolean isFirstResource)，但不要每次请求都使用新的监听器，要避免不必要的内存申请，
		可以使用单例进行统一的异常监听和处理。
	skipMemoryCache(boolean skip). 设置是否跳过内存缓存，但不保证一定不被缓存（比如请求已经在加载资源且没设置跳过内存缓存，
		这个资源就会被缓存在内存中）。
	override(int width, int height). 重新设置Target的宽高值（单位为pixel）。
	into(Y target).设置资源将被加载到的Target。
	into(ImageView view). 设置资源将被加载到的ImageView。取消该ImageView之前所有的加载并释放资源。
	into(int width, int height). 后台线程加载时要加载资源的宽高值（单位为pixel）。
	preload(int width, int height). 预加载resource到缓存中（单位为pixel）。
	asBitmap(). 无论资源是不是gif动画，都作为Bitmap对待。如果是gif动画会停在第一帧。
	asGif().把资源作为GifDrawable对待。如果资源不是gif动画将会失败，会回调.error()。
	
	
技巧：

禁止内存缓存：
    .skipMemoryCache(true)

清除内存缓存：
    // 必须在UI线程中调用
    Glide.get(context).clearMemory();

禁止磁盘缓存：

   .diskCacheStrategy(DiskCacheStrategy.NONE)

清除磁盘缓存：

   // 必须在后台线程中调用，建议同时clearMemory()
   Glide.get(applicationContext).clearDiskCache();

获取缓存大小：

   new GetDiskCacheSizeTask(textView).execute(new File(getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));

GetDiskCacheSizeTask源码   
	class GetDiskCacheSizeTask extends AsyncTask<File, Long, Long> {
	private final TextView resultView;

	public GetDiskCacheSizeTask(TextView resultView) {
		this.resultView = resultView;
	}

	@Override
	protected void onPreExecute() {
		resultView.setText("Calculating...");
	}

	@Override
	protected void onProgressUpdate(Long... values) { /* onPostExecute(values[values.length - 1]); */ }

	@Override
	protected Long doInBackground(File... dirs) {
		try {
			long totalSize = 0;
			for (File dir : dirs) {
				publishProgress(totalSize);
				totalSize += calculateSize(dir);
			}
			return totalSize;
		} catch (RuntimeException ex) {
			final String message = String.format("Cannot get size of %s: %s", Arrays.toString(dirs), ex);
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					resultView.setText("error");
					Toast.makeText(resultView.getContext(), message, Toast.LENGTH_LONG).show();
				}
			});
		}
		return 0L;
	}

	@Override
	protected void onPostExecute(Long size) {
		String sizeText = android.text.format.Formatter.formatFileSize(resultView.getContext(), size);
		resultView.setText(sizeText);
	}

	private static long calculateSize(File dir) {
		if (dir == null) return 0;
		if (!dir.isDirectory()) return dir.length();
		long result = 0;
		File[] children = dir.listFiles();
		if (children != null)
			for (File child : children)
				result += calculateSize(child);
		return result;
	}
	}


指定资源的优先加载顺序：
    //优先加载
    Glide
        .with(context)
        .load(heroImageUrl)
        .priority(Priority.HIGH)
        .into(imageViewHero);
    //后加载
    Glide
        .with(context)
        .load(itemImageUrl)
        .priority(Priority.LOW)
        .into(imageViewItem);

先显示缩略图，再显示原图：
    //用原图的1/10作为缩略图
    Glide
        .with(this)
        .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
        .thumbnail(0.1f)
        .into(iv_0);
    //用其它图片作为缩略图
    DrawableRequestBuilder<Integer> thumbnailRequest = Glide
        .with(this)
        .load(R.drawable.news);
    Glide.with(this)
        .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
        .thumbnail(thumbnailRequest)
        .into(iv_0);

		
================================================================================================================		
Glide可以对图片进行裁剪、模糊、滤镜等处理： 
	推荐使用独立的图片处理库：wasabeef/glide-transformations，使用也很简单：
    compile 'jp.wasabeef:glide-transformations:2.0.0'

之后我们就可以使用GenericRequestBuilder或其子类的transform()或bitmapTransform()方法设置图片转换了：

    //圆形裁剪
    Glide.with(this)
        .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
        .bitmapTransform(new CropCircleTransformation(this))
        .into(iv_0);
    //圆角处理
    Glide.with(this)
        .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
        .bitmapTransform(new RoundedCornersTransformation(this,30,0, RoundedCornersTransformation.CornerType.ALL))
        .into(iv_0);
    //灰度处理
    Glide.with(this)
        .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
        .bitmapTransform(new GrayscaleTransformation(this))
        .into(iv_0);
    //其它变换...


可根据情况使用GenericRequestBuilder子类DrawableRequestBuilder的bitmapTransform(Transformation<Bitmap>... 
bitmapTransformations)，transform(BitmapTransformation... transformations)，transform(Transformation<GifBitmapWrapper>... 
transformation)，或其子类BitmapRequestBuilder的transform(BitmapTransformation... transformations)，
transform(Transformation<Bitmap>... transformations)方法。 

当然如果想自己写Transformation： 
最简单的方式就是继承BitmapTransformation：

private static class MyTransformation extends BitmapTransformation {

    public MyTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, 
            int outWidth, int outHeight) {
       Bitmap myTransformedBitmap = ... //对Bitmap进行各种变换处理。
       return myTransformedBitmap;
    }

    @Override
    public String getId() {
        // 返回代表该变换的唯一Id，会作为cache key的一部分。
        // 注意：最好不要用getClass().getName()，因为容易受混淆影响。如果变换过程不影响缓存数据，可以返回空字符串。
        return "com.example.myapp.MyTransformation";
    }
}


使用时只需使用transform()或bitmapTransform()方法即可：

	Glide.with(yourFragment)
		.load(yourUrl)
		.asBitmap()
		.transform(new MyTransformation(context))
		.into(yourView);

自定义图片处理时Glide会自动计算View/Target大小，我们不需要传View的宽高，当然你可以使用override(int, int)去改变这种行为。
自定义图片处理时，为了避免创建大量Bitmap以及减少GC，可以考虑重用Bitmap，这就需要BitmapPool，典型地就是，
从Bitmap池中拿一个Bitmap，用这个Bitmap生成一个Canvas, 然后在这个Canvas上画初始的Bitmap并使用Matrix、Paint、
或者Shader处理这张图片。 

为了有效并正确重用Bitmap需要遵循以下三条准则：
	1，永远不要把transform()传给你的原始resource或原始Bitmap给recycle()了，更不要放回BitmapPool，因为这些都自动完成了。
		值得注意的是，任何从BitmapPool取出的用于自定义图片变换的辅助Bitmap，
		如果不经过transform()方法返回，就必须主动放回BitmapPool或者调用recycle()回收。
	
	2，如果你从BitmapPool拿出多个Bitmap或不使用你从BitmapPool拿出的一个Bitmap，一定要返回extras给BitmapPool。

	3，如果你的图片处理没有替换原始resource(例如由于一张图片已经匹配了你想要的尺寸，你需要提前返回), 
		transform()`方法就返回原始resource或原始Bitmap。 
如：
    private static class MyTransformation extends BitmapTransformation {
        public MyTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            // 如果BitmapPool中找不到符合该条件的Bitmap，get()方法会返回null，就需要我们自己创建Bitmap了
            if (result == null) {
                // 如果想让Bitmap支持透明度，就需要使用ARGB_8888
                result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            }
            //创建最终Bitmap的Canvas.
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setAlpha(128);
            // 将原始Bitmap处理后画到最终Bitmap中
            canvas.drawBitmap(toTransform, 0, 0, paint);
            // 由于我们的图片处理替换了原始Bitmap，就return我们新的Bitmap就行。
            // Glide会自动帮我们回收原始Bitmap。
            return result;
        }

        @Override
        public String getId() {
            // Return some id that uniquely identifies your transformation.
            return "com.example.myapp.MyTransformation";
        }
    }


也可以直接实现Transformation接口，进行更灵活的图片处理，如进行简单地圆角处理：

public class RoundedCornersTransformation  implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    private int mRadius;

    public RoundedCornersTransformation(Context context, int mRadius) {
        this(Glide.get(context).getBitmapPool(), mRadius);
    }

    public RoundedCornersTransformation(BitmapPool mBitmapPool, int mRadius) {
        this.mBitmapPool = mBitmapPool;
        this.mRadius = mRadius;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        //从其包装类中拿出Bitmap
        Bitmap source = resource.get();
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        //以上已经算是教科书式写法了
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);
        //返回包装成Resource的最终Bitmap
        return BitmapResource.obtain(result, mBitmapPool);
    }

    @Override
    public String getId() {
        return "RoundedTransformation(radius=" + mRadius + ")";
    }
}


对请求状态进行监听：

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ImageView iv_0;
    private LoggingListener mCommonRequestListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_0 = (ImageView) findViewById(R.id.iv_0);
        mCommonRequestListener = new LoggingListener<String, GlideDrawable>();
        Glide
                .with(this)
                .load("http://inthecheesefactory.com/uploads/source/nestedfragment/fragments.png")
                .listener(mCommonRequestListener)
                .into(iv_0);
}
}


/**
* @param <A> model类型
* @param <B> resource类型
*/
public class LoggingListener<A, B> implements RequestListener<A, B> {
private final int level;
private final String name;
private final RequestListener<A, B> delegate;

public LoggingListener() {
    this("");
}

public LoggingListener(@NonNull String name) {
    this(Log.VERBOSE, name);
}

public LoggingListener(int level, @NonNull String name) {
    this(level, name, null);
}

public LoggingListener(RequestListener<A, B> delegate) {
    this(Log.VERBOSE, "", delegate);
}

public LoggingListener(int level, @NonNull String name, RequestListener<A, B> delegate) {
    this.level = level;
    this.name = name;
    this.delegate = delegate == null ? NoOpRequestListener.<A, B>get() : delegate;
}

@Override
public boolean onException(Exception e, A model, Target<B> target, boolean isFirstResource) {
    android.util.Log.println(level, "GLIDE", String.format(Locale.ROOT,
            "%s.onException(%s, %s, %s, %s)\n%s",
            name, e, model, strip(target), isFirst(isFirstResource), android.util.Log.getStackTraceString(e)));
    return delegate.onException(e, model, target, isFirstResource);
}

@Override
public boolean onResourceReady(B resource, A model, Target<B> target, boolean isFromMemoryCache,
                               boolean isFirstResource) {
    String resourceString = strip(getResourceDescription(resource));
    String targetString = strip(getTargetDescription(target));
    android.util.Log.println(level, "GLIDE", String.format(Locale.ROOT,
            "%s.onResourceReady(%s, %s, %s, %s, %s)",
            name, resourceString, model, targetString, isMem(isFromMemoryCache), isFirst(isFirstResource)));
    return delegate.onResourceReady(resource, model, target, isFromMemoryCache, isFirstResource);
}

private String isMem(boolean isFromMemoryCache) {
    return isFromMemoryCache ? "sync" : "async";
}

private String isFirst(boolean isFirstResource) {
    return isFirstResource ? "first" : "not first";
}

private String getTargetDescription(Target<B> target) {
    String result;
    if (target instanceof ViewTarget) {
        View v = ((ViewTarget) target).getView();
        LayoutParams p = v.getLayoutParams();
        result = String.format(Locale.ROOT,
                "%s(params=%dx%d->size=%dx%d)", target, p.width, p.height, v.getWidth(), v.getHeight());
    } else {
        result = String.valueOf(target);
    }
    return result;
}

private String getResourceDescription(B resource) {
    String result;
    if (resource instanceof Bitmap) {
        Bitmap bm = (Bitmap) resource;
        result = String.format(Locale.ROOT,
                "%s(%dx%d@%s)", resource, bm.getWidth(), bm.getHeight(), bm.getConfig());
    } else if (resource instanceof BitmapDrawable) {
        Bitmap bm = ((BitmapDrawable) resource).getBitmap();
        result = String.format(Locale.ROOT,
                "%s(%dx%d@%s)", resource, bm.getWidth(), bm.getHeight(), bm.getConfig());
    } else if (resource instanceof GlideBitmapDrawable) {
        Bitmap bm = ((GlideBitmapDrawable) resource).getBitmap();
        result = String.format(Locale.ROOT,
                "%s(%dx%d@%s)", resource, bm.getWidth(), bm.getHeight(), bm.getConfig());
    } else if (resource instanceof Drawable) {
        Drawable d = (Drawable) resource;
        result = String.format(Locale.ROOT,
                "%s(%dx%d)", resource, d.getIntrinsicWidth(), d.getIntrinsicHeight());
    } else {
        result = String.valueOf(resource);
    }
    return result;
}

private static String strip(Object text) {
    return String.valueOf(text).replaceAll("(com|android|net|org)(\\.[a-z]+)+\\.", "");
}
}


public final class NoOpRequestListener<A, B> implements RequestListener<A, B> {
private static final RequestListener INSTANCE = new NoOpRequestListener();

@SuppressWarnings("unchecked")
public static <A, B> RequestListener<A, B> get() {
    return INSTANCE;
}

private NoOpRequestListener() {
}

@Override public boolean onException(Exception e, A a, Target<B> target, boolean b) {
    return false;
}
@Override public boolean onResourceReady(B b, A a, Target<B> target, boolean b2, boolean b1) {
    return false;
}
}



通过GenericRequestBuilder的listener()方法添加一个RequestListener实现，但要注意，最好不要用匿名类，
也不要每次都创建新的监听器，要使用单例进行统一监听处理，以避免不必要的内存申请和不必要的引用。
方法最好返回false，以便Glide能继续进行后续处理(如显示error占位符)。

对资源的下载进度进行监听： 
可以借助OkHttp的拦截器进行进度监听。OkHttp的拦截器官方Sample请移步这里。我们可以利用这个拦截器进行监听并处理，
需要自定义ModelLoader和DataFetcher，具体请详见我的Git：https://github.com/shangmingchao/ProgressGlide，欢迎Star啊，
不过没有太大必要告诉用户图片加载的进度（sjudd和TWiStErRob他们说的）。同时也可以看一下TWiStErRob大神的实现（自备梯子哈~.~）。










+

