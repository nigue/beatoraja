package bms.player.beatoraja;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Logger;

import com.badlogic.gdx.Graphics;
import bms.player.beatoraja.config.KeyConfiguration;
import bms.player.beatoraja.decide.MusicDecide;
import bms.player.beatoraja.input.BMSPlayerInputProcessor;
import bms.player.beatoraja.play.BMSPlayer;
import bms.player.beatoraja.result.GradeResult;
import bms.player.beatoraja.result.MusicResult;
import bms.player.beatoraja.select.MusicSelector;
import bms.player.beatoraja.song.SQLiteSongDatabaseAccessor;
import bms.player.beatoraja.song.SongDatabaseAccessor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import static bms.player.beatoraja.Resolution.*;

public class MainController extends ApplicationAdapter {

	public static final String VERSION = "beatoraja 0.3.6";

	private BMSPlayer player;
	private MusicDecide decide;
	private MusicSelector selector;
	private MusicResult result;
	private GradeResult gresult;
	private KeyConfiguration keyconfig;

	private PlayerResource resource;

	private BitmapFont systemfont;

	private MainState current;

	private Config config;
	private int auto;

	private SongDatabaseAccessor songdb;

	private SpriteBatch sprite;
	private ShapeRenderer shape;
	/**
	 * 1曲プレイで指定したBMSファイル
	 */
	private Path bmsfile;

	private BMSPlayerInputProcessor input;
	/**
	 * FPSを描画するかどうか
	 */
	private boolean showfps;
	/**
	 * プレイデータアクセサ
	 */
	private PlayDataAccessor playdata;

	static final Path configpath = Paths.get("config.json");
	private static final Path songdbpath = Paths.get("songdata.db");

	private ScreenShotThread screenshot;

	public MainController(Path f, Config config, int auto) {
		this.auto = auto;
		this.config = config;
		this.bmsfile = f;

		try {
			Class.forName("org.sqlite.JDBC");
			songdb = new SQLiteSongDatabaseAccessor(songdbpath.toString(), config.getBmsroot());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		playdata = new PlayDataAccessor("playerscore");
	}

	public SongDatabaseAccessor getSongDatabase() {
		return songdb;
	}

	public PlayDataAccessor getPlayDataAccessor() {
		return playdata;
	}

	public SpriteBatch getSpriteBatch() {
		return sprite;
	}

	public ShapeRenderer getShapeRenderer() {
		return shape;
	}

	public PlayerResource getPlayerResource() {
		return resource;
	}

	public static final int STATE_SELECTMUSIC = 0;
	public static final int STATE_DECIDE = 1;
	public static final int STATE_PLAYBMS = 2;
	public static final int STATE_RESULT = 3;
	public static final int STATE_GRADE_RESULT = 4;
	public static final int STATE_CONFIG = 5;

	public void changeState(int state) {
		MainState newState = null;
		switch (state) {
		case STATE_SELECTMUSIC:
			if (this.bmsfile != null) {
				exit();
			}
			newState = selector;
			break;
		case STATE_DECIDE:
			newState = decide;
			break;
		case STATE_PLAYBMS:
			if (player != null) {
				player.dispose();
			}
			player = new BMSPlayer(this, resource);
			newState = player;
			break;
		case STATE_RESULT:
			newState = result;
			break;
		case STATE_GRADE_RESULT:
			newState = gresult;
			break;
		case STATE_CONFIG:
			newState = keyconfig;
			break;
		}

		if (newState != null && current != newState) {
			Arrays.fill(newState.getTimer(), Long.MIN_VALUE);
			newState.create();
			newState.getSkin().prepare(newState);
			current = newState;
			current.setStartTime(System.currentTimeMillis());
		}
		if (current.getStage() != null) {
			Gdx.input.setInputProcessor(new InputMultiplexer(current.getStage(), input.getKeyBoardInputProcesseor()));
		} else {
			Gdx.input.setInputProcessor(input.getKeyBoardInputProcesseor());
		}
	}

	public void setAuto(int auto) {
		this.auto = auto;

	}

	@Override
	public void create() {
		final long t = System.currentTimeMillis();
		sprite = new SpriteBatch();
		shape = new ShapeRenderer();

		input = new BMSPlayerInputProcessor(RESOLUTION[config.getResolution()]);
		
		selector = new MusicSelector(this, config);
		decide = new MusicDecide(this);
		result = new MusicResult(this);
		gresult = new GradeResult(this);
		keyconfig = new KeyConfiguration(this);
		resource = new PlayerResource(config);
		if (bmsfile != null) {
			resource.setBMSFile(bmsfile, config, auto);
			changeState(STATE_PLAYBMS);
		} else {
			changeState(STATE_SELECTMUSIC);
		}

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/default/VL-Gothic-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 24;
		systemfont = generator.generateFont(parameter);
		generator.dispose();
		Logger.getGlobal().info("初期化時間(ms) : " + (System.currentTimeMillis() - t));
	}

	private long prevtime;

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		current.render();
		sprite.begin();
		current.getSkin().drawAllObjects(sprite, current);
		sprite.end();

		final Stage stage = current.getStage();
		if (stage != null) {
			stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
			stage.draw();
		}
		
		// show fps
		if (showfps) {
			sprite.begin();
			systemfont.setColor(Color.PURPLE);
			systemfont.draw(sprite, String.format("FPS %d", Gdx.graphics.getFramesPerSecond()), 10,
					RESOLUTION[config.getResolution()].height - 2);
			sprite.end();
		}
		// show screenshot status
		if (screenshot != null && screenshot.savetime + 2000 > System.currentTimeMillis()) {
			sprite.begin();
			systemfont.setColor(Color.GOLD);
			systemfont.draw(sprite, "Screen shot saved : " + screenshot.path, 100,
					RESOLUTION[config.getResolution()].height - 2);
			sprite.end();
		}

		final long time = System.currentTimeMillis();
		if(time > prevtime) {
		    prevtime = time;
            current.input();
            // move song bar position by mouse
            if (input.isMousePressed()) {
                input.setMousePressed();
                current.getSkin().mousePressed(current, input.getMouseButton(), input.getMouseX(), input.getMouseY());
            }
            if (input.isMouseDragged()) {
                input.setMouseDragged();
                current.getSkin().mouseDragged(current, input.getMouseButton(), input.getMouseX(), input.getMouseY());
            }

            // FPS表示切替
            if (input.getFunctionstate()[0] && input.getFunctiontime()[0] != 0) {
                showfps = !showfps;
                input.getFunctiontime()[0] = 0;
            }
            // fullscrees - windowed
            if (input.getFunctionstate()[3] && input.getFunctiontime()[3] != 0) {
                boolean fullscreen = Gdx.graphics.isFullscreen();
                Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                if (fullscreen) {
                    Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
                } else {
                    Gdx.graphics.setFullscreenMode(currentMode);
                }
                config.setFullscreen(!fullscreen);
                input.getFunctiontime()[3] = 0;
            }

            // if (input.getFunctionstate()[4] && input.getFunctiontime()[4] != 0) {
            // int resolution = config.getResolution();
            // resolution = (resolution + 1) % RESOLUTION.length;
            // if (config.isFullscreen()) {
            // Gdx.graphics.setWindowedMode((int) RESOLUTION[resolution].width,
            // (int) RESOLUTION[resolution].height);
            // Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
            // Gdx.graphics.setFullscreenMode(currentMode);
            // }
            // else {
            // Gdx.graphics.setWindowedMode((int) RESOLUTION[resolution].width,
            // (int) RESOLUTION[resolution].height);
            // }
            // config.setResolution(resolution);
            // input.getFunctiontime()[4] = 0;
            // }

            // screen shot
            if (input.getFunctionstate()[5] && input.getFunctiontime()[5] != 0) {
                if (screenshot == null || screenshot.savetime != 0) {
                    screenshot = new ScreenShotThread(ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(),
                            Gdx.graphics.getBackBufferHeight(), true));
                    screenshot.start();
                }
                input.getFunctiontime()[5] = 0;
            }
        }
	}

	@Override
	public void dispose() {
		// shape.dispose();
		// sprite.dispose();
		if (player != null) {
			player.dispose();
		}
		if (selector != null) {
			selector.dispose();
		}
		if (decide != null) {
			decide.dispose();
		}
		if (result != null) {
			result.dispose();
		}
		if (gresult != null) {
			gresult.dispose();
		}
		if (keyconfig != null) {
			keyconfig.dispose();
		}
		resource.dispose();
	}

	@Override
	public void pause() {
		current.pause();
	}

	@Override
	public void resize(int width, int height) {
		current.resize(width, height);
	}

	@Override
	public void resume() {
		current.resume();
	}

	public void exit() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		try {
			FileWriter fw = new FileWriter(configpath.toFile());
			fw.write(json.prettyPrint(config));
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dispose();
		Gdx.app.exit();
	}

	public BMSPlayerInputProcessor getInputProcessor() {
		return input;
	}

	/**
	 * スクリーンショット処理用スレッド
	 * 
	 * @author exch
	 */
	class ScreenShotThread extends Thread {

		/**
		 * 処理が完了した時間
		 */
		private long savetime;
		/**
		 * スクリーンショット保存先
		 */
		private final String path;
		/**
		 * スクリーンショットのpixelデータ
		 */
		private final byte[] pixels;
		
		public ScreenShotThread(byte[] pixels) {
			this.pixels = pixels;
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			path = "screenshot/" + sdf.format(Calendar.getInstance().getTime()) + ".png";
		}

		@Override
		public void run() {
			Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(),
					Pixmap.Format.RGBA8888);
			BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
			PixmapIO.writePNG(new FileHandle(path), pixmap);
			pixmap.dispose();
			input.getFunctiontime()[5] = 0;
			Logger.getGlobal().info("スクリーンショット保存:" + path);
			screenshot.savetime = System.currentTimeMillis();
		}
	}
}
