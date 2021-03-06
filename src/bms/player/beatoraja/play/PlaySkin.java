package bms.player.beatoraja.play;

import bms.player.beatoraja.skin.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static bms.player.beatoraja.skin.SkinProperty.*;

/**
 * プレイスキン
 * 
 * @author exch
 */
public class PlaySkin extends Skin {

	private int playstart;

	private TextureRegion[][] gauge;

	private TextureRegion[][] judge;

	private TextureRegion[][][] judgenum;

	private SkinGraph[] graph;

	private SkinImage[] line = new SkinImage[0];
	/**
	 * レーン描画エリア
	 */
	private Rectangle[] laneregion;

	private Rectangle[] lanegroupregion;

	private int judgeregion;

	private float dw;
	private float dh;

	private int close;
	
	private int loadstart;
	private int loadend;

	private final int[] judgecount = { NUMBER_EARLY_PERFECT, NUMBER_LATE_PERFECT,
			NUMBER_EARLY_GREAT, NUMBER_LATE_GREAT, NUMBER_EARLY_GOOD,
			NUMBER_LATE_GOOD, NUMBER_EARLY_BAD, NUMBER_LATE_BAD,
			NUMBER_EARLY_POOR, NUMBER_LATE_POOR, NUMBER_EARLY_MISS,
			NUMBER_LATE_MISS };

	protected BMSPlayer player;
	
	private static final int[] fixop = {OPTION_STAGEFILE, OPTION_NO_STAGEFILE, OPTION_BACKBMP, OPTION_NO_BACKBMP, 
		OPTION_AUTOPLAYON, OPTION_AUTOPLAYOFF, OPTION_BGAON, OPTION_BGAOFF, 
		OPTION_BGANORMAL, OPTION_BGAEXTEND, OPTION_GAUGE_GROOVE, OPTION_GAUGE_HARD,
		OPTION_SCOREGRAPHOFF, OPTION_SCOREGRAPHON,OPTION_DIFFICULTY0,OPTION_DIFFICULTY1
		,OPTION_DIFFICULTY2,OPTION_DIFFICULTY3,OPTION_DIFFICULTY4,OPTION_DIFFICULTY5,
		OPTION_NO_BPMCHANGE,OPTION_BPMCHANGE};

	public PlaySkin(float srcw, float srch, float dstw, float dsth) {
		super(srcw, srch, dstw, dsth, fixop);
	}

	public PlaySkin(int mode, boolean use2p, Rectangle r) {
		super(1280, 720, r.width, r.height, fixop);
		dw = r.width / 1280f;
		dh = r.height / 720f;

		makeCommonSkin();
		if (mode == 5 || mode == 7) {
			if (use2p) {
				make7KeySkin2();
			} else {
				make7KeySkin();
			}
		} else if (mode == 10 || mode == 14) {
			make14KeySkin();
		} else {
			make9KeySkin();
		}

		// 閉店
		Texture close = new Texture("skin/default/close.png");
		SkinImage ci = new SkinImage(new TextureRegion(close, 0, 500, 640, 240));
		setDestination(ci, 0, 0, -360, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 700, TIMER_FAILED, 0, 0,
				0);
		setDestination(ci, 500, 0, 0, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ci, 600, 0, -40, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ci, 700, 0, 0, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ci);
		ci = new SkinImage(new TextureRegion(close, 0, 740, 640, 240));
		setDestination(ci, 0, 0, 720, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 700, TIMER_FAILED, 0, 0,
				0);
		setDestination(ci, 500, 0, 360, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ci, 600, 0, 400, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ci, 700, 0, 360, 1280, 360, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ci);

		Texture nt = new Texture("skin/default/system.png");
		SkinImage fi = new SkinImage(new TextureRegion(nt, 0, 0, 8, 8));
		setDestination(fi, 0, 0, 0, 1280, 720, 0, 0, 255, 255, 255, 0, 0, 0, 0, 500, TIMER_FADEOUT, 0, 0, 0);
		setDestination(fi, 500, 0, 0, 1280, 720, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(fi);

		setPlaystart(1000);
		setClose(1500);
		setFadeout(1000);
	}

	private void makeCommonSkin() {
		SkinImage back = new SkinImage(new TextureRegion(new Texture("skin/default/play.png")));
		setDestination(back, 0, 0, 0, 1280, 720, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(back);
		Texture bg = new Texture("skin/default/playbg.png");
		SkinImage images = new SkinImage(new TextureRegion(bg));
		setDestination(images, 0, 0, 0, 1280, 720, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(images);
		// ゲージ
		Texture gt = new Texture("skin/default/gauge.png");
		gauge = new TextureRegion[1][8];
		gauge[0][0] = new TextureRegion(gt, 0, 0, 5, 17);
		gauge[0][1] = new TextureRegion(gt, 5, 0, 5, 17);
		gauge[0][2] = new TextureRegion(gt, 10, 0, 5, 17);
		gauge[0][3] = new TextureRegion(gt, 15, 0, 5, 17);
		gauge[0][4] = new TextureRegion(gt, 0, 17, 5, 17);
		gauge[0][5] = new TextureRegion(gt, 5, 17, 5, 17);
		gauge[0][6] = new TextureRegion(gt, 10, 17, 5, 17);
		gauge[0][7] = new TextureRegion(gt, 15, 17, 5, 17);
		// 判定文字
		Texture jt = new Texture("skin/default/judge.png");
		judge = new TextureRegion[6][];
		judge[0] = new TextureRegion[] { new TextureRegion(jt, 0, 0, 180, 50), new TextureRegion(jt, 0, 50, 180, 50) };
		judge[1] = new TextureRegion[] { new TextureRegion(jt, 0, 150, 180, 50), new TextureRegion(jt, 0, 350, 180, 50) };
		judge[2] = new TextureRegion[] { new TextureRegion(jt, 0, 200, 180, 50), new TextureRegion(jt, 0, 350, 180, 50) };
		judge[3] = new TextureRegion[] { new TextureRegion(jt, 0, 250, 180, 50), new TextureRegion(jt, 0, 350, 180, 50) };
		judge[4] = new TextureRegion[] { new TextureRegion(jt, 0, 300, 180, 50), new TextureRegion(jt, 0, 350, 180, 50) };
		judgenum = new Sprite[3][2][10];
		for (int i = 0; i < 10; i++) {
			judgenum[0][0][i] = new Sprite(jt, 30 * i + 200, 0, 30, 50);
			judgenum[0][1][i] = new Sprite(jt, 30 * i + 200, 50, 30, 50);
			judgenum[1][0][i] = new Sprite(jt, 30 * i + 200, 150, 30, 50);
			judgenum[1][1][i] = new Sprite(jt, 30 * i + 200, 350, 30, 50);
			judgenum[2][0][i] = new Sprite(jt, 30 * i + 200, 200, 30, 50);
			judgenum[2][1][i] = new Sprite(jt, 30 * i + 200, 350, 30, 50);
		}
		// 数字
		Texture nt = new Texture("skin/default/number.png");
		TextureRegion[][] ntr = TextureRegion.split(nt, 24, 24);

		// bpm
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_MINBPM), 0, 520, 2, 18, 18, 0, 255, 255, 255, 255,
				0, 0, 0, 0, 0, 0, OPTION_BPMCHANGE, 0, 0);
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_NOWBPM), 0, 592, 2, 24, 24, 0, 255, 255, 255, 255,
				0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_MAXBPM), 0, 688, 2, 18, 18, 0, 255, 255, 255, 255,
				0, 0, 0, 0, 0, 0, OPTION_BPMCHANGE, 0, 0);
		// 残り時間
		addNumber(new SkinNumber(ntr[0], 2, 1, NUMBER_TIMELEFT_MINUTE), 0, 1148, 2, 24, 24, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 2, 1, NUMBER_TIMELEFT_SECOND), 0, 1220, 2, 24, 24, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		addNumber(new SkinNumber(ntr[0], 2, 0, NUMBER_HISPEED), 0, 116, 2, 12, 24, 0, 255, 255, 255, 255,
				0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 2, 1, NUMBER_HISPEED_AFTERDOT), 0, 154, 2, 10, 20, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_DURATION), 0, 318, 2, 12, 24, 0, 255, 255, 255, 255,
				0, 0, 0, 0, 0, 0, 0, 0, 0);

		Texture grapht = new Texture("skin/default/scoregraph.png");
		TextureRegion[] graphtr = new TextureRegion[3];
		graphtr[0] = new TextureRegion(grapht, 0, 0, 100, 296);
		graphtr[1] = new TextureRegion(grapht, 100, 0, 100, 296);
		graphtr[2] = new TextureRegion(grapht, 200, 0, 100, 296);

		graph = new SkinGraph[3];
		graph[0] = new SkinGraph(new TextureRegion(grapht, 0, 0, 100, 296));
		graph[0].setReferenceID(BARGRAPH_SCORERATE);
		graph[1] = new SkinGraph(new TextureRegion(grapht, 100, 0, 100, 296));
		graph[1].setReferenceID(BARGRAPH_BESTSCORERATE);
		graph[2] = new SkinGraph(new TextureRegion(grapht, 200, 0, 100, 296));
		graph[2].setReferenceID(BARGRAPH_TARGETSCORERATE);

	}

	private void make7KeySkin() {
		// 背景
		// background = new Texture("skin/default/bg.jpg");
		// ノーツ
		TextureRegion[][] note = new TextureRegion[8][];
		TextureRegion[][][] longnote = new TextureRegion[10][8][];
		TextureRegion[][] minenote = new TextureRegion[8][];
		Texture notet = new Texture("skin/default/note.png");
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 99, 5, 27, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 99, 43, 27, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 99, 57, 27, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 99, 80, 27, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 99, 76, 27, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 99, 94, 27, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 99, 108, 27, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 99, 131, 27, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 99, 127, 27, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 99, 128, 27, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 99, 129, 27, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 99, 23, 27, 8) };
			} else if (i == 7) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 50, 5, 46, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 50, 43, 46, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 50, 57, 46, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 50, 80, 46, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 50, 76, 46, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 50, 94, 46, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 50, 108, 46, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 50, 131, 46, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 50, 127, 46, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 50, 128, 46, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 50, 129, 46, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 50, 23, 46, 8) };
			} else {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 127, 5, 21, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 127, 43, 21, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 127, 57, 21, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 127, 80, 21, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 127, 76, 21, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 127, 94, 21, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 127, 108, 21, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 127, 131, 21, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 127, 127, 21, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 127, 128, 21, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 127, 129, 21, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 127, 23, 21, 8) };
			}
		}

		SkinImage[] images = new SkinImage[6];
		SkinNumber[] number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage();
			images[i].setImage(judge[i == 5 ? 4 : i], 0, 100);
			setDestination(images[i], 0, 115, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(images[i], 500, 115, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(number[i], 500, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
		}

		SkinBGA bga = new SkinBGA(this);
		setDestination(bga, 0, 500, 50, 740, 650, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga);

		SkinGauge sgauge = new SkinGauge(gauge,0,0);
		setDestination(sgauge, 0, 20, 30, 390, 30, 0, 255,255,255,255,0,0,0,0,0,0,0,0,0);
		add(sgauge);

		SkinText title = new SkinText("skin/default/VL-Gothic-Regular.ttf", 0, 24, 2);
		title.setReferenceID(STRING_FULLTITLE);
		setDestination(title, 0, 502, 698, 24, 24, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 1000, 502, 698, 24, 24, 0, 0, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 2000, 502, 698, 24, 24, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(title);

		laneregion = new Rectangle[8];
		laneregion[0] = rect(90, 140, 50, 580);
		laneregion[1] = rect(140, 140, 40, 580);
		laneregion[2] = rect(180, 140, 50, 580);
		laneregion[3] = rect(230, 140, 40, 580);
		laneregion[4] = rect(270, 140, 50, 580);
		laneregion[5] = rect(320, 140, 40, 580);
		laneregion[6] = rect(360, 140, 50, 580);
		laneregion[7] = rect(20, 140, 70, 580);
		Texture st = new Texture("skin/default/system.png");
		SkinImage si = new SkinImage(new TextureRegion(st, 30, 0, 390, 10));
		setDestination(si, 0, 20, 140, 390, 0, 0, 0, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		setDestination(si, 1000, 20, 140, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(si);

		// graph
		SkinImage gbi = new SkinImage(new TextureRegion(st, 168, 108, 126, 303));
		setDestination(gbi, 0, 410, 220, 90, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gbi);

		setDestination(graph[0], 0, 411, 220, 28, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[0]);
		setDestination(graph[1], 0, 441, 220, 28, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[1]);
		setDestination(graph[2], 0, 471, 220, 28, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[2]);

		SkinImage gi = new SkinImage(new TextureRegion(st, 40, 108, 126, 303));
		setDestination(gi, 0, 410, 220, 90, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gi);

		// progress
		SkinImage progress = new SkinImage(new TextureRegion(st, 10, 10, 10, 251));
		setDestination(progress, 0, 4, 140, 12, 540, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(progress);
		SkinSlider pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 0, 289, 14, 20) }, 0, 0, 2,
				(int) (520 * dh), SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 2, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
		add(pseek);
		pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 15, 289, 14, 20) }, 0, 0, 2, (int) (520 * dh),
				SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 2, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_ENDOFNOTE_1P, 0,
				0, 0);
		add(pseek);

		Texture kbt = new Texture("skin/default/keybeam.png");
		Sprite[] keybeam = new Sprite[8];
		keybeam[0] = keybeam[2] = keybeam[4] = keybeam[6] = new Sprite(kbt, 47, 0, 28, 255);
		keybeam[1] = keybeam[3] = keybeam[5] = new Sprite(kbt, 75, 0, 21, 255);
		keybeam[7] = new Sprite(kbt, 0, 0, 47, 255);
		TextureRegion[] keybeaml = new Sprite[8];
		keybeaml[0] = keybeaml[2] = keybeaml[4] = keybeaml[6] = new Sprite(kbt, 144, 0, 28, 255);
		keybeaml[1] = keybeaml[3] = keybeaml[5] = new Sprite(kbt, 172, 0, 21, 255);
		keybeaml[7] = new Sprite(kbt, 97, 0, 47, 255);
		Texture bombt = new Texture("skin/default/bomb.png");
		TextureRegion[][] bombtr = TextureRegion.split(bombt, 181, 192);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage ri = new SkinImage(new TextureRegion[][] { { keybeam[i] }, { keybeaml[i] } }, 0, 0);
			setDestination(ri, 0, laneregion[i].x / dw + laneregion[i].width / dw / 4, laneregion[i].y / dh,
					laneregion[i].width / dw / 2, laneregion[i].height / dh, 0, 255, 255, 255, 255, 0, 0, 0, 0, 100,
					TIMER_KEYON_1P_KEY1 + (i % 8 == 7 ? -1 : i), 0, 0, 0);
			setDestination(ri, 100, laneregion[i].x / dw, laneregion[i].y / dh, laneregion[i].width / dw,
					laneregion[i].height / dh, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			ri.setOffsety(OFFSET_LIFT);
			ri.setReferenceID(VALUE_JUDGE_1P_KEY1 + (i % 8 == 7 ? -1 : i));
			add(ri);
		}
		SkinImage judgeline = new SkinImage(new TextureRegion(st, 16, 0, 8, 8));
		judgeline.setOffsety(OFFSET_LIFT);
		setDestination(judgeline, 0, 20, 137, 390, 6, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(judgeline);
		line = new SkinImage[1];
		line[0] = new SkinImage(new TextureRegion(st, 0, 0, 1, 1));
		line[0].setOffsety(OFFSET_LIFT);
		setDestination(line[0], 0, 20, 140, 390, 1, 0, 255, 128, 128, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(new SkinNote(note, longnote, minenote, 12 / dh));
		judgeregion = 1;
		add(new SkinJudge(images, number, 0, true));
		Texture lct = new Texture("skin/default/lanecover.png");
		SkinSlider lanecover = new SkinSlider(new TextureRegion[] { new TextureRegion(lct) }, 0, 0, 2, (int) (580 * dh),
				SLIDER_LANECOVER);
		setDestination(lanecover, 0, 20, 720, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(lanecover);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage bombi = new SkinImage(new TextureRegion[][] { {}, bombtr[3], bombtr[0], bombtr[1] }, TIMER_BOMB_1P_KEY1 + (i % 8 == 7 ? -1 : i), 160);
			setDestination(bombi, 0, laneregion[i].x / dw + laneregion[i].width / dw / 2 - 141, laneregion[i].y / dh
					- 202, 322, 344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 161, TIMER_BOMB_1P_KEY1
					+ (i % 8 == 7 ? -1 : i), 0, 0, 0);
			setDestination(bombi, 160, laneregion[i].x / dw + laneregion[i].width / dw / 2 - 141, laneregion[i].y / dh
					- 202, 322, 344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
			setDestination(bombi, 161, laneregion[i].x / dw, laneregion[i].y / dh, 0, 0, 0, 255, 255, 255, 255, 2, 0,
					0, 0, 0, 0, 0, 0, 0);
			bombi.setOffsety(OFFSET_LIFT);
			bombi.setReferenceID(VALUE_JUDGE_1P_KEY1 + (i % 8 == 7 ? -1 : i));
			add(bombi);

			SkinImage hbombi = new SkinImage(bombtr[2], TIMER_HOLD_1P_KEY1 + (i % 8 == 7 ? -1 : i), 160);
			setDestination(hbombi, 0, laneregion[i].x / dw + laneregion[i].width / dw / 2 - 141, laneregion[i].y / dh
					- 202, 322, 344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_HOLD_1P_KEY1
					+ (i % 8 == 7 ? -1 : i), 0, 0, 0);
			hbombi.setOffsety(OFFSET_LIFT);
			add(hbombi);
		}

		// judge count
		Texture nt = new Texture("skin/default/number.png");
		TextureRegion[][] ntr = TextureRegion.split(nt, 24, 24);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				addNumber(new SkinNumber(ntr[j + 1], 4, 2, judgecount[i * 2 + j]), 0, 536 + j * 60,
						50 + (5 - i) * 18, 12, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			}
		}
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_SCORE_RATE), 0, 420, 200, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_SCORE_RATE_AFTERDOT), 0, 468, 200, 8, 12, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 5, 0, NUMBER_SCORE), 0, 420, 180, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[1], 5, 0, NUMBER_HIGHSCORE), 0, 420, 160, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[2], 5, 0, NUMBER_TARGET_SCORE), 0, 420, 140, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		lanegroupregion = new Rectangle[] { rect(20, 140, 390, 580) };

		SkinGraph seek = new SkinGraph(new TextureRegion(st, 0, 0, 8, 8));
		seek.setReferenceID(BARGRAPH_LOAD_PROGRESS);
		seek.setDirection(0);
		setDestination(seek, 0, 20, 440, 390, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 500, 20, 440, 390, 4, 0, 192, 0, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1000, 20, 440, 390, 4, 0, 128, 255, 0, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1500, 20, 440, 390, 4, 0, 192, 255, 255, 0, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 2000, 20, 440, 390, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		add(seek);
		// READY
		Texture ready = new Texture("skin/default/ready.png");
		SkinImage ri = new SkinImage(new TextureRegion(ready));
		setDestination(ri, 0, 40, 250, 350, 60, 0, 0, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_READY, 0, 0, 0);
		setDestination(ri, 750, 40, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ri, 1000, 40, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ri);

		TextureRegion[] wv = new TextureRegion[11];
		TextureRegion[] yv = new TextureRegion[11];
		for (int i = 0; i < 11; i++) {
			wv[i] = new TextureRegion(st, i * 10, 550, 10, 15);
			yv[i] = new TextureRegion(st, i * 10, 565, 10, 15);
		}
		SkinNumber white = new SkinNumber(wv, 3, 0, NUMBER_LANECOVER1);
		white.setOffsety(OFFSET_LANECOVER);
		setDestination(white, 0, 120, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(white);
		SkinNumber yellow = new SkinNumber(yv,4, 0, NUMBER_DURATION);
		yellow.setOffsety(OFFSET_LANECOVER);
		setDestination(yellow, 0, 310, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(yellow);

		addNumber(new SkinNumber(ntr[0], 3, 0, NUMBER_GROOVEGAUGE), 0, 314, 60, 24, 24, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_GROOVEGAUGE_AFTERDOT), 0, 386, 60, 18, 18, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

	}

	private void make7KeySkin2() {
		// 背景
		// background = new Texture("skin/default/bg.jpg");
		// ノーツ
		TextureRegion[][] note = new TextureRegion[8][];
		TextureRegion[][][] longnote = new TextureRegion[10][8][];
		TextureRegion[][] minenote = new TextureRegion[8][];
		Texture notet = new Texture("skin/default/note.png");
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 99, 5, 27, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 99, 43, 27, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 99, 57, 27, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 99, 80, 27, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 99, 76, 27, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 99, 94, 27, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 99, 108, 27, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 99, 131, 27, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 99, 127, 27, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 99, 128, 27, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 99, 129, 27, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 99, 23, 27, 8) };
			} else if (i == 7) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 50, 5, 46, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 50, 43, 46, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 50, 57, 46, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 50, 80, 46, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 50, 76, 46, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 50, 94, 46, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 50, 108, 46, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 50, 131, 46, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 50, 127, 46, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 50, 128, 46, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 50, 129, 46, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 50, 23, 46, 8) };
			} else {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 127, 5, 21, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 127, 43, 21, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 127, 57, 21, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 127, 80, 21, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 127, 76, 21, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 127, 94, 21, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 127, 108, 21, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 127, 131, 21, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 127, 127, 21, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 127, 128, 21, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 127, 129, 21, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 127, 23, 21, 8) };
			}
		}

		SkinImage[] images = new SkinImage[6];
		SkinNumber[] number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage(judge[i == 5 ? 4 : i],0,100);
			setDestination(images[i], 0, 965, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(images[i], 500, 965, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(number[i], 500, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
		}

		SkinBGA bga = new SkinBGA(this);
		setDestination(bga, 0, 40, 50, 740, 650, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga);

		SkinGauge sgauge = new SkinGauge(gauge,0,0);
		setDestination(sgauge, 0, 1260, 30, -390, 30, 0, 255,255,255,255,0,0,0,0,0,0,0,0,0);
		add(sgauge);

		SkinText title = new SkinText("skin/default/VL-Gothic-Regular.ttf", 0, 24, 2);
		title.setReferenceID(STRING_FULLTITLE);
		setDestination(title, 0, 42, 698, 24, 24, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 1000, 42, 698, 24, 24, 0, 0, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 2000, 42, 698, 24, 24, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(title);

		laneregion = new Rectangle[8];
		laneregion[0] = rect(870, 140, 50, 580);
		laneregion[1] = rect(920, 140, 40, 580);
		laneregion[2] = rect(960, 140, 50, 580);
		laneregion[3] = rect(1010, 140, 40, 580);
		laneregion[4] = rect(1050, 140, 50, 580);
		laneregion[5] = rect(1100, 140, 40, 580);
		laneregion[6] = rect(1140, 140, 50, 580);
		laneregion[7] = rect(1190, 140, 70, 580);
		Texture st = new Texture("skin/default/system.png");
		SkinImage si = new SkinImage(new TextureRegion(st, 30, 0, 390, 10));
		setDestination(si, 0, 1260, 140, -390, 0, 0, 0, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		setDestination(si, 1000, 1260, 140, -390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(si);

		// graph
		SkinImage gbi = new SkinImage(new TextureRegion(st, 168, 108, 126, 303));
		setDestination(gbi, 0, 780, 220, 90, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gbi);

		setDestination(graph[0], 0, 841, 220, 28, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[0]);
		setDestination(graph[1], 0, 811, 220, 28, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[1]);
		setDestination(graph[2], 0, 781, 220, 28, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[2]);

		SkinImage gi = new SkinImage(new TextureRegion(st, 40, 108, 126, 303));
		setDestination(gi, 0, 780, 220, 90, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gi);

		// progress
		SkinImage progress = new SkinImage(new TextureRegion(st, 10, 10, 10, 251));
		setDestination(progress, 0, 1264, 140, 12, 540, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(progress);
		SkinSlider pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 0, 289, 14, 20) }, 0, 0, 2,
				(int) (520 * dh), SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 1264, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
		add(pseek);
		pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 15, 289, 14, 20) }, 0, 0, 2, (int) (520 * dh),
				SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 1264, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_ENDOFNOTE_1P,
				0, 0, 0);
		add(pseek);

		Texture kbt = new Texture("skin/default/keybeam.png");
		Sprite[] keybeam = new Sprite[8];
		keybeam[0] = keybeam[2] = keybeam[4] = keybeam[6] = new Sprite(kbt, 47, 0, 28, 255);
		keybeam[1] = keybeam[3] = keybeam[5] = new Sprite(kbt, 75, 0, 21, 255);
		keybeam[7] = new Sprite(kbt, 0, 0, 47, 255);
		TextureRegion[] keybeaml = new Sprite[8];
		keybeaml[0] = keybeaml[2] = keybeaml[4] = keybeaml[6] = new Sprite(kbt, 144, 0, 28, 255);
		keybeaml[1] = keybeaml[3] = keybeaml[5] = new Sprite(kbt, 172, 0, 21, 255);
		keybeaml[7] = new Sprite(kbt, 97, 0, 47, 255);
		Texture bombt = new Texture("skin/default/bomb.png");
		TextureRegion[][] bombtr = TextureRegion.split(bombt, 181, 192);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage ri = new SkinImage(new TextureRegion[][] { { keybeam[i] }, { keybeaml[i] } }, 0, 0);
			setDestination(ri, 0, laneregion[i].x / dw + laneregion[i].width / dw / 4, laneregion[i].y / dh,
					laneregion[i].width / dw / 2, laneregion[i].height / dh, 0, 255, 255, 255, 255, 0, 0, 0, 0, 100,
					TIMER_KEYON_1P_KEY1 + (i % 8 == 7 ? -1 : i), 0, 0, 0);
			setDestination(ri, 100, laneregion[i].x / dw, laneregion[i].y / dh, laneregion[i].width / dw,
					laneregion[i].height / dh, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			ri.setOffsety(OFFSET_LIFT);
			ri.setReferenceID(VALUE_JUDGE_1P_KEY1 + (i % 8 == 7 ? -1 : i));
			add(ri);
		}
		SkinImage judgeline = new SkinImage(new TextureRegion(st, 16, 0, 8, 8));
		judgeline.setOffsety(OFFSET_LIFT);
		setDestination(judgeline, 0, 870, 137, 390, 6, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(judgeline);
		line = new SkinImage[1];
		line[0] = new SkinImage(new TextureRegion(st, 0, 0, 1, 1));
		line[0].setOffsety(OFFSET_LIFT);
		setDestination(line[0], 0, 870, 140, 390, 1, 0, 255, 128, 128, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(new SkinNote(note, longnote, minenote, 12 / dh));
		add(new SkinJudge(images, number, 0, true));
		judgeregion = 1;

		Texture lct = new Texture("skin/default/lanecover.png");
		SkinSlider lanecover = new SkinSlider(new TextureRegion[] { new TextureRegion(lct) }, 0, 0, 2, (int) (580 * dh),
				SLIDER_LANECOVER);
		setDestination(lanecover, 0, 870, 720, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(lanecover);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage bombi = new SkinImage(new TextureRegion[][] { {}, bombtr[3], bombtr[0], bombtr[1] },TIMER_BOMB_1P_KEY1 + (i % 8 == 7 ? -1 : i), 160);
			setDestination(bombi, 0, laneregion[i].x / dw + laneregion[i].width / dw / 2 - 141, laneregion[i].y / dh
					- 202, 322, 344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 161, TIMER_BOMB_1P_KEY1
					+ (i % 8 == 7 ? -1 : i), 0, 0, 0);
			setDestination(bombi, 160, laneregion[i].x / dw + laneregion[i].width / dw / 2 - 141, laneregion[i].y / dh
					- 202, 322, 344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
			setDestination(bombi, 161, laneregion[i].x / dw, laneregion[i].y / dh, 0, 0, 0, 255, 255, 255, 255, 2, 0,
					0, 0, 0, 0, 0, 0, 0);
			bombi.setOffsety(OFFSET_LIFT);
			bombi.setReferenceID(VALUE_JUDGE_1P_KEY1 + (i % 8 == 7 ? -1 : i));
			add(bombi);

			SkinImage hbombi = new SkinImage(bombtr[2], TIMER_HOLD_1P_KEY1 + (i % 8 == 7 ? -1 : i),160);
			setDestination(hbombi, 0, laneregion[i].x / dw + laneregion[i].width / dw / 2 - 141, laneregion[i].y / dh
					- 202, 322, 344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_HOLD_1P_KEY1
					+ (i % 8 == 7 ? -1 : i), 0, 0, 0);
			hbombi.setOffsety(OFFSET_LIFT);
			add(hbombi);
		}

		// judge count
		Texture nt = new Texture("skin/default/number.png");
		TextureRegion[][] ntr = TextureRegion.split(nt, 24, 24);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				addNumber(new SkinNumber(ntr[j + 1], 4, 2, judgecount[i * 2 + j]), 0, 660 + j * 60,
						50 + (5 - i) * 18, 12, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			}
		}
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_SCORE_RATE), 0, 790, 200, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_SCORE_RATE_AFTERDOT), 0, 838, 200, 8, 12, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 5, 0, NUMBER_SCORE), 0, 790, 180, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[1], 5, 0, NUMBER_HIGHSCORE), 0, 790, 160, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[2], 5, 0, NUMBER_TARGET_SCORE), 0, 790, 140, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		lanegroupregion = new Rectangle[] { rect(870, 140, 390, 580) };

		SkinGraph seek = new SkinGraph(new TextureRegion(st, 0, 0, 8, 8));
		seek.setReferenceID(BARGRAPH_LOAD_PROGRESS);
		seek.setDirection(0);
		setDestination(seek, 0, 870, 440, 390, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 500, 870, 440, 390, 4, 0, 192, 0, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1000, 870, 440, 390, 4, 0, 128, 255, 0, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1500, 870, 440, 390, 4, 0, 192, 255, 255, 0, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 2000, 870, 440, 390, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		add(seek);

		// READY
		Texture ready = new Texture("skin/default/ready.png");
		SkinImage ri = new SkinImage(new TextureRegion(ready));
		setDestination(ri, 0, 870, 250, 350, 60, 0, 0, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_READY, 0, 0, 0);
		setDestination(ri, 750, 870, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ri, 1000, 870, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ri);

		TextureRegion[] wv = new TextureRegion[11];
		TextureRegion[] yv = new TextureRegion[11];
		for (int i = 0; i < 11; i++) {
			wv[i] = new TextureRegion(st, i * 10, 550, 10, 15);
			yv[i] = new TextureRegion(st, i * 10, 565, 10, 15);
		}
		SkinNumber white = new SkinNumber(wv, 3, 0, NUMBER_LANECOVER1);
		white.setOffsety(OFFSET_LANECOVER);
		setDestination(white, 0, 970, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(white);
		SkinNumber yellow = new SkinNumber(yv,4, 0, NUMBER_DURATION);
		yellow.setOffsety(OFFSET_LANECOVER);
		setDestination(yellow, 0, 1160, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(yellow);

		addNumber(new SkinNumber(ntr[0], 3, 0, NUMBER_GROOVEGAUGE), 0, 870, 60, 24, 24, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_GROOVEGAUGE_AFTERDOT), 0, 942, 60, 18, 18, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

	}

	private void make9KeySkin() {
		// 背景
		// background = new Texture("skin/default/bg.jpg");
		// ノーツ
		TextureRegion[][] note = new TextureRegion[9][];
		TextureRegion[][][] longnote = new TextureRegion[10][9][];
		TextureRegion[][] minenote = new TextureRegion[9][];
		Texture notet = new Texture("skin/default/pop.png");
		for (int i = 0; i < 9; i++) {
			if (i == 0 || i == 8) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 0, 0, 36, 18) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 0, 18, 36, 18) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 0, 38, 36, 18) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 0, 38, 36, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 0, 36, 36, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 0, 18, 36, 18) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 0, 38, 36, 18) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 0, 38, 36, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 0, 36, 36, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 0, 38, 36, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 0, 64, 36, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 0, 56, 36, 18) };
			}
			if (i == 1 || i == 7) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 38, 0, 28, 18) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 38, 18, 28, 18) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 38, 38, 28, 18) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 38, 38, 28, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 38, 36, 28, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 38, 18, 28, 18) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 38, 38, 28, 18) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 38, 38, 28, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 38, 36, 28, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 38, 38, 28, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 38, 64, 28, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 38, 56, 28, 18) };
			}
			if (i == 2 || i == 6) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 68, 0, 36, 18) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 68, 18, 36, 18) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 68, 38, 36, 18) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 68, 38, 36, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 68, 36, 36, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 68, 18, 36, 18) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 68, 38, 36, 18) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 68, 38, 36, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 68, 36, 36, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 68, 38, 36, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 68, 64, 36, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 68, 56, 36, 18) };
			}
			if (i == 3 || i == 5) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 106, 0, 28, 18) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 106, 18, 28, 18) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 106, 38, 28, 18) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 106, 38, 28, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 106, 36, 28, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 106, 18, 28, 18) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 106, 38, 28, 18) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 106, 38, 28, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 106, 36, 28, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 106, 38, 28, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 106, 64, 28, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 106, 56, 28, 18) };
			}
			if (i == 4) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 136, 0, 36, 18) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 136, 18, 36, 18) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 136, 38, 36, 18) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 136, 38, 36, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 136, 36, 36, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 136, 18, 36, 18) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 136, 38, 36, 18) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 136, 38, 36, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 136, 36, 36, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 136, 38, 36, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 136, 64, 36, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 136, 56, 36, 18) };
			}
		}

		SkinBGA bga = new SkinBGA(this);
		setDestination(bga, 0, 10, 390, 330, 330, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga);
		SkinBGA bga2 = new SkinBGA(this);
		setDestination(bga2, 0, 10, 50, 330, 330, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga2);

		SkinGauge sgauge = new SkinGauge(gauge,0,0);
		setDestination(sgauge, 0, 345, 30, 590, 30, 0, 255,255,255,255,0,0,0,0,0,0,0,0,0);
		add(sgauge);

		SkinText title = new SkinText("skin/default/VL-Gothic-Regular.ttf", 0, 24);
		title.setReferenceID(STRING_FULLTITLE);
		setDestination(title, 0, 12, 720, 18, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 1000, 12, 720, 18, 18, 0, 0, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 2000, 12, 720, 18, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(title);

		laneregion = new Rectangle[9];
		laneregion[0] = rect(345, 140, 70, 580);
		laneregion[1] = rect(415, 140, 60, 580);
		laneregion[2] = rect(475, 140, 70, 580);
		laneregion[3] = rect(545, 140, 60, 580);
		laneregion[4] = rect(605, 140, 70, 580);
		laneregion[5] = rect(675, 140, 60, 580);
		laneregion[6] = rect(735, 140, 70, 580);
		laneregion[7] = rect(805, 140, 60, 580);
		laneregion[8] = rect(865, 140, 70, 580);
		Texture st = new Texture("skin/default/system.png");
		SkinImage si = new SkinImage(new TextureRegion(st, 30, 30, 590, 10));
		setDestination(si, 0, 345, 140, 590, 0, 0, 0, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		setDestination(si, 1000, 345, 140, 590, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(si);

		// graph
		SkinImage gbi = new SkinImage(new TextureRegion(st, 168, 108, 126, 303));
		setDestination(gbi, 0, 960, 220, 180, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gbi);
		setDestination(graph[0], 0, 962, 220, 56, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[0]);
		setDestination(graph[1], 0, 1022, 220, 56, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[1]);
		setDestination(graph[2], 0, 1082, 220, 56, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[2]);

		SkinImage gi = new SkinImage(new TextureRegion(st, 40, 108, 126, 303));
		setDestination(gi, 0, 960, 220, 180, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gi);

		Texture kbt = new Texture("skin/default/keybeaml.png");
		Sprite[] keybeam = new Sprite[9];
		keybeam[0] = keybeam[2] = keybeam[4] = keybeam[6] = keybeam[8] = new Sprite(kbt, 75, 0, 21, 255);
		keybeam[1] = keybeam[3] = keybeam[5] = keybeam[7] = new Sprite(kbt, 47, 0, 28, 255);
		TextureRegion[] keybeaml = new Sprite[9];
		keybeaml[0] = keybeaml[2] = keybeaml[4] = keybeaml[6] = keybeaml[8] = new Sprite(kbt, 172, 0, 21, 255);
		keybeaml[1] = keybeaml[3] = keybeaml[5] = keybeaml[7] = new Sprite(kbt, 144, 0, 28, 255);
		TextureRegion[] keybeamg = new Sprite[9];
		keybeamg[0] = keybeamg[2] = keybeamg[4] = keybeamg[6] = keybeamg[8] = new Sprite(kbt, 269, 0, 21, 255);
		keybeamg[1] = keybeamg[3] = keybeamg[5] = keybeamg[7] = new Sprite(kbt, 241, 0, 28, 255);

		Texture bombt = new Texture("skin/default/bomb.png");
		TextureRegion[][] bombtr = TextureRegion.split(bombt, 181, 192);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage bi = new SkinImage(new TextureRegion[][] { { keybeam[i] }, { keybeaml[i] }, { keybeamg[i] },
					{ keybeamg[i] } }, 0, 0);
			setDestination(bi, 0, laneregion[i].x + laneregion[i].width / 4, laneregion[i].y, laneregion[i].width / 2,
					laneregion[i].height, 0, 255, 255, 255, 255, 0, 0, 0, 0, 100, TIMER_KEYON_1P_KEY1 + i, 0,
					0, 0);
			setDestination(bi, 100, laneregion[i].x, laneregion[i].y, laneregion[i].width, laneregion[i].height, 0,
					255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			bi.setOffsety(OFFSET_LIFT);
			bi.setReferenceID(VALUE_JUDGE_1P_KEY1 + i);
			add(bi);

		}

		SkinImage line = new SkinImage(new TextureRegion(st, 16, 0, 8, 8));
		line.setOffsety(OFFSET_LIFT);
		setDestination(line, 0, 345, 137, 590, 6, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(line);
		add(new SkinNote(note, longnote, minenote, 18 / dh));
		SkinImage[] images = new SkinImage[6];
		SkinNumber[] number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage(judge[i == 5 ? 4 : i], 0, 100);
			setDestination(images[i], 0, 375, 240, 140, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(images[i], 500, 375, 240, 140, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 70, -30, 20, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(number[i], 500, 70, -30, 20, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
		}
		add(new SkinJudge(images, number, 0, false));
		images = new SkinImage[6];
		number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage(judge[i == 5 ? 4 : i], 0, 100);
			setDestination(images[i], 0, 570, 240, 140, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
			setDestination(images[i], 500, 570, 240, 140, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 70, -30, 20, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
			setDestination(number[i], 500, 70, -30, 20, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
		}
		add(new SkinJudge(images, number, 1, false));
		images = new SkinImage[6];
		number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage(judge[i == 5 ? 4 : i], 0, 100);
			setDestination(images[i], 0, 765, 240, 140, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_3P, 0, 0, 0);
			setDestination(images[i], 500, 765, 240, 140, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_3P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 70, -30, 20, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_3P, 0, 0, 0);
			setDestination(number[i], 500, 70, -30, 20, 20, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_3P, 0, 0, 0);
		}
		add(new SkinJudge(images, number, 2, false));
		judgeregion = 3;
		Texture lct = new Texture("skin/default/lanecover.png");
		SkinSlider lanecover = new SkinSlider(new TextureRegion[] { new TextureRegion(lct) }, 0, 0, 2, (int) (580 * dh),
				SLIDER_LANECOVER);
		setDestination(lanecover, 0, 345, 720, 590, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(lanecover);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage bombi = new SkinImage(bombtr[0], TIMER_BOMB_1P_KEY1 + i, 160);
			setDestination(bombi, 0, laneregion[i].x + laneregion[i].width / 2 - 156, laneregion[i].y - 222, 362, 384,
					0, 255, 255, 255, 255, 2, 0, 0, 0, 161, TIMER_BOMB_1P_KEY1 + i, 0, 0, 0);
			setDestination(bombi, 160, laneregion[i].x + laneregion[i].width / 2 - 156, laneregion[i].y - 222, 362,
					384, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
			setDestination(bombi, 161, laneregion[i].x, laneregion[i].y, 0, 0, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0,
					0, 0, 0);
			bombi.setOffsety(OFFSET_LIFT);
			add(bombi);

			SkinImage hbombi = new SkinImage(bombtr[2], TIMER_HOLD_1P_KEY1 + i, 160);
			setDestination(hbombi, 0, laneregion[i].x + laneregion[i].width / 2 - 156, laneregion[i].y - 222, 362, 384,
					0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_HOLD_1P_KEY1 + i, 0, 0, 0);
			hbombi.setOffsety(OFFSET_LIFT);
			add(hbombi);
		}

		// judge count
		Texture nt = new Texture("skin/default/number.png");
		TextureRegion[][] ntr = TextureRegion.split(nt, 24, 24);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				addNumber(new SkinNumber(ntr[j + 1], 4, 2, judgecount[i * 2 + j]), 0, 1126 + j * 60,
						30 + (5 - i) * 18, 12, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			}
		}
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_SCORE_RATE), 0, 970, 200, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_SCORE_RATE_AFTERDOT), 0, 1018, 200, 8, 12, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 5, 0, NUMBER_SCORE), 0, 970, 180, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[1], 5, 0, NUMBER_HIGHSCORE), 0, 970, 160, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[2], 5, 0, NUMBER_TARGET_SCORE), 0, 970, 140, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		SkinGraph seek = new SkinGraph(new TextureRegion(st, 0, 0, 8, 8));
		seek.setReferenceID(BARGRAPH_LOAD_PROGRESS);
		seek.setDirection(0);
		setDestination(seek, 0, 345, 440, 590, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 500, 345, 440, 590, 4, 0, 192, 0, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1000, 345, 440, 590, 4, 0, 128, 255, 0, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1500, 345, 440, 590, 4, 0, 192, 255, 255, 0, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 2000, 345, 440, 590, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		add(seek);
		// READY
		Texture ready = new Texture("skin/default/ready.png");
		SkinImage ri = new SkinImage(new TextureRegion(ready));
		setDestination(ri, 0, 465, 250, 350, 60, 0, 0, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_READY, 0, 0, 0);
		setDestination(ri, 750, 465, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ri, 1000, 465, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ri);

		TextureRegion[] wv = new TextureRegion[11];
		TextureRegion[] yv = new TextureRegion[11];
		for (int i = 0; i < 11; i++) {
			wv[i] = new TextureRegion(st, i * 10, 550, 10, 15);
			yv[i] = new TextureRegion(st, i * 10, 565, 10, 15);
		}
		SkinNumber white = new SkinNumber(wv, 3, 0, NUMBER_LANECOVER1);
		white.setOffsety(OFFSET_LANECOVER);
		setDestination(white, 0, 445, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(white);
		SkinNumber yellow = new SkinNumber(yv,4, 0, NUMBER_DURATION);
		yellow.setOffsety(OFFSET_LANECOVER);
		setDestination(yellow, 0, 835, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(yellow);

		lanegroupregion = new Rectangle[] { rect(345, 140, 590, 580) };

		addNumber(new SkinNumber(ntr[0],3, 0, NUMBER_GROOVEGAUGE), 0, 600, 60, 24, 24, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0],1, 0, NUMBER_GROOVEGAUGE_AFTERDOT), 0, 672, 60, 18, 18, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		// progress
		SkinImage progress = new SkinImage(new TextureRegion(st, 10, 10, 10, 251));
		setDestination(progress, 0, 940, 140, 12, 540, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(progress);
		SkinSlider pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 0, 289, 14, 20) }, 0, 0, 2,
				(int) (520 * dh), SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 938, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
		add(pseek);
		pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 15, 289, 14, 20) }, 0, 0, 2, (int) (520 * dh),
				SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 938, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_ENDOFNOTE_1P,
				0, 0, 0);
		add(pseek);
	}

	private void make14KeySkin() {
		// 背景
		// background = new Texture("skin/default/bg.jpg");
		// ノーツ
		TextureRegion[][] note = new TextureRegion[16][];
		TextureRegion[][][] longnote = new TextureRegion[10][16][];
		TextureRegion[][] minenote = new TextureRegion[16][];
		Texture notet = new Texture("skin/default/note.png");
		for (int i = 0; i < 16; i++) {
			if (i % 2 == 0) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 99, 5, 27, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 99, 43, 27, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 99, 57, 27, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 99, 80, 27, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 99, 76, 27, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 99, 94, 27, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 99, 108, 27, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 99, 131, 27, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 99, 127, 27, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 99, 128, 27, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 99, 129, 27, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 99, 23, 27, 8) };
			} else if (i == 7 || i == 15) {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 50, 5, 46, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 50, 43, 46, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 50, 57, 46, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 50, 80, 46, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 50, 76, 46, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 50, 94, 46, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 50, 108, 46, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 50, 131, 46, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 50, 127, 46, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 50, 128, 46, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 50, 129, 46, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 50, 23, 46, 8) };
			} else {
				note[i] = new TextureRegion[] { new TextureRegion(notet, 127, 5, 21, 12) };
				longnote[0][i] = new TextureRegion[] { new TextureRegion(notet, 127, 43, 21, 13) };
				longnote[1][i] = new TextureRegion[] { new TextureRegion(notet, 127, 57, 21, 13) };
				longnote[2][i] = new TextureRegion[] { new TextureRegion(notet, 127, 80, 21, 1) };
				longnote[3][i] = new TextureRegion[] { new TextureRegion(notet, 127, 76, 21, 1) };
				longnote[4][i] = new TextureRegion[] { new TextureRegion(notet, 127, 94, 21, 13) };
				longnote[5][i] = new TextureRegion[] { new TextureRegion(notet, 127, 108, 21, 13) };
				longnote[6][i] = new TextureRegion[] { new TextureRegion(notet, 127, 131, 21, 1) };
				longnote[7][i] = new TextureRegion[] { new TextureRegion(notet, 127, 127, 21, 1) };
				longnote[8][i] = new TextureRegion[] { new TextureRegion(notet, 127, 128, 21, 1) };
				longnote[9][i] = new TextureRegion[] { new TextureRegion(notet, 127, 129, 21, 1) };
				minenote[i] = new TextureRegion[] { new TextureRegion(notet, 127, 23, 21, 8) };
			}
		}

		Texture kbt = new Texture("skin/default/keybeam.png");
		Sprite[] keybeam = new Sprite[16];
		keybeam[0] = keybeam[2] = keybeam[4] = keybeam[6] = keybeam[8] = keybeam[10] = keybeam[12] = keybeam[14] = new Sprite(
				kbt, 75, 0, 21, 255);
		keybeam[1] = keybeam[3] = keybeam[5] = keybeam[9] = keybeam[11] = keybeam[13] = new Sprite(kbt, 47, 0, 28, 255);
		keybeam[7] = keybeam[15] = new Sprite(kbt, 0, 0, 47, 255);

		SkinBGA bga = new SkinBGA(this);
		setDestination(bga, 0, 10, 500, 180, 220, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga);
		SkinBGA bga2 = new SkinBGA(this);
		setDestination(bga2, 0, 10, 270, 180, 220, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga2);
		SkinBGA bga3 = new SkinBGA(this);
		setDestination(bga3, 0, 10, 40, 180, 220, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(bga3);

		SkinGauge sgauge = new SkinGauge(gauge,0,0);
		setDestination(sgauge, 0, 445, 30, 390, 30, 0, 255,255,255,255,0,0,0,0,0,0,0,0,0);
		add(sgauge);

		SkinText title = new SkinText("skin/default/VL-Gothic-Regular.ttf", 0, 24);
		title.setReferenceID(STRING_FULLTITLE);
		setDestination(title, 0, 12, 720, 18, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 1000, 12, 720, 18, 18, 0, 0, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(title, 2000, 12, 720, 18, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(title);

		laneregion = new Rectangle[16];
		laneregion[0] = rect(280, 140, 50, 580);
		laneregion[1] = rect(330, 140, 40, 580);
		laneregion[2] = rect(370, 140, 50, 580);
		laneregion[3] = rect(420, 140, 40, 580);
		laneregion[4] = rect(460, 140, 50, 580);
		laneregion[5] = rect(510, 140, 40, 580);
		laneregion[6] = rect(550, 140, 50, 580);
		laneregion[7] = rect(210, 140, 70, 580);
		laneregion[8] = rect(680, 140, 50, 580);
		laneregion[9] = rect(730, 140, 40, 580);
		laneregion[10] = rect(770, 140, 50, 580);
		laneregion[11] = rect(820, 140, 40, 580);
		laneregion[12] = rect(860, 140, 50, 580);
		laneregion[13] = rect(910, 140, 40, 580);
		laneregion[14] = rect(950, 140, 50, 580);
		laneregion[15] = rect(1000, 140, 70, 580);

		Texture st = new Texture("skin/default/system.png");
		SkinImage si = new SkinImage(new TextureRegion(st, 30, 0, 390, 10));
		setDestination(si, 0, 210, 140, 390, 0, 0, 0, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		setDestination(si, 1000, 210, 140, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(si);
		si = new SkinImage(new TextureRegion(st, 30, 15, 390, 10));
		setDestination(si, 0, 680, 140, 390, 0, 0, 0, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		setDestination(si, 1000, 680, 140, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(si);

		// graph
		SkinImage gbi = new SkinImage(new TextureRegion(st, 168, 108, 126, 303));
		setDestination(gbi, 0, 1090, 220, 180, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gbi);

		setDestination(graph[0], 0, 1092, 220, 56, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[0]);
		setDestination(graph[1], 0, 1152, 220, 56, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[1]);
		setDestination(graph[2], 0, 1212, 220, 56, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(graph[2]);

		SkinImage gi = new SkinImage(new TextureRegion(st, 40, 108, 126, 303));
		setDestination(gi, 0, 1090, 220, 180, 480, 0, 255, 255, 255, 255, 0, 0, 0, 0, 1000, 0, 0, 0, 0);
		add(gi);

		Texture bombt = new Texture("skin/default/bomb.png");
		TextureRegion[][] bombtr = TextureRegion.split(bombt, 181, 192);
		for (int i = 0; i < laneregion.length; i++) {
			SkinImage ri = new SkinImage(keybeam[i]);
			setDestination(ri, 0, laneregion[i].x + laneregion[i].width / 4, laneregion[i].y, laneregion[i].width / 2,
					laneregion[i].height, 0, 255, 255, 255, 255, 0, 0, 0, 0, 100, TIMER_KEYON_1P_KEY1
							+ (i % 8 == 7 ? -1 : (i % 8)) + (i >= 8 ? 10 : 0), 0, 0, 0);
			setDestination(ri, 100, laneregion[i].x, laneregion[i].y, laneregion[i].width, laneregion[i].height, 0,
					255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			ri.setOffsety(OFFSET_LIFT);
			add(ri);
		}

		SkinImage judgeline = new SkinImage(new TextureRegion(st, 16, 0, 8, 8));
		judgeline.setOffsety(OFFSET_LIFT);
		setDestination(judgeline, 0, 210, 137, 390, 6, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(judgeline);
		judgeline = new SkinImage(new TextureRegion(st, 16, 0, 8, 8));
		judgeline.setOffsety(OFFSET_LIFT);
		setDestination(judgeline, 0, 680, 137, 390, 6, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(judgeline);
		line = new SkinImage[2];
		line[0] = new SkinImage(new TextureRegion(st, 0, 0, 1, 1));
		setDestination(line[0], 0, 210, 140, 390, 1, 0, 255, 128, 128, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		line[0].setOffsety(OFFSET_LIFT);
		line[1] = new SkinImage(new TextureRegion(st, 0, 0, 1, 1));
		setDestination(line[1], 0, 680, 140, 390, 1, 0, 255, 128, 128, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		line[1].setOffsety(OFFSET_LIFT);
		add(new SkinNote(note, longnote, minenote, 12 / dh));
		SkinImage[] images = new SkinImage[6];
		SkinNumber[] number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage(judge[i == 5 ? 4 : i], 0, 100);
			setDestination(images[i], 0, 315, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(images[i], 500, 315, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
			setDestination(number[i], 500, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_1P, 0, 0, 0);
		}
		add(new SkinJudge(images, number, 0, true));
		images = new SkinImage[6];
		number = new SkinNumber[6];
		for (int i = 0; i < 6; i++) {
			images[i] = new SkinImage(judge[i == 5 ? 4 : i], 0 ,100);
			setDestination(images[i], 0, 785, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
			setDestination(images[i], 500, 785, 240, 180, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
			images[i].setOffsety(OFFSET_LIFT);
			number[i] = new SkinNumber(judgenum[i > 2 ? 2 : i], 0, 100, 6, 0, NUMBER_MAXCOMBO);
			setDestination(number[i], 0, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
			setDestination(number[i], 500, 200, 0, 40, 40, 0, 255, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_JUDGE_2P, 0, 0, 0);
		}
		add(new SkinJudge(images, number, 1, true));

		judgeregion = 2;
		Texture lct = new Texture("skin/default/lanecover.png");
		SkinSlider lanecover = new SkinSlider(new TextureRegion[] { new TextureRegion(lct) }, 0, 0, 2, (int) (580 * dh),
				SLIDER_LANECOVER);
		setDestination(lanecover, 0, 210, 720, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(lanecover);
		SkinSlider lanecover2 = new SkinSlider(new TextureRegion[] { new TextureRegion(lct) }, 0, 0, 2, (int) (580 * dh),
				SLIDER_LANECOVER);
		setDestination(lanecover2, 0, 680, 720, 390, 580, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(lanecover2);

		for (int i = 0; i < laneregion.length; i++) {
			SkinImage bombi = new SkinImage(new TextureRegion[][] { {}, bombtr[3], bombtr[0], bombtr[1] }, TIMER_BOMB_1P_KEY1 + (i % 8 == 7 ? -1 : (i % 8)) + (i >= 8 ? 10 : 0), 160);
			setDestination(bombi, 0, laneregion[i].x + laneregion[i].width / 2 - 141, laneregion[i].y - 202, 322, 344,
					0, 255, 255, 255, 255, 2, 0, 0, 0, 161, TIMER_BOMB_1P_KEY1 + (i % 8 == 7 ? -1 : (i % 8))
							+ (i >= 8 ? 10 : 0), 0, 0, 0);
			setDestination(bombi, 160, laneregion[i].x + laneregion[i].width / 2 - 141, laneregion[i].y - 202, 322,
					344, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
			setDestination(bombi, 161, laneregion[i].x, laneregion[i].y, 0, 0, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0,
					0, 0, 0);
			bombi.setOffsety(OFFSET_LIFT);
			bombi.setReferenceID(VALUE_JUDGE_1P_KEY1 + (i % 8 == 7 ? -1 : (i % 8)) + (i >= 8 ? 10 : 0));
			add(bombi);

			SkinImage hbombi = new SkinImage(bombtr[2], TIMER_HOLD_1P_KEY1 + (i % 8 == 7 ? -1 : (i % 8)) + (i >= 8 ? 10 : 0), 160);
			setDestination(hbombi, 0, laneregion[i].x + laneregion[i].width / 2 - 141, laneregion[i].y - 202, 322, 344,
					0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_HOLD_1P_KEY1 + (i % 8 == 7 ? -1 : (i % 8))
							+ (i >= 8 ? 10 : 0), 0, 0, 0);
			hbombi.setOffsety(OFFSET_LIFT);
			add(hbombi);
		}

		// judge count
		Texture nt = new Texture("skin/default/number.png");
		TextureRegion[][] ntr = TextureRegion.split(nt, 24, 24);
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				addNumber(new SkinNumber(ntr[j + 1], 4, 2, judgecount[i * 2 + j]), 0, 1166 + j * 60,
						40 + (5 - i) * 18, 12, 18, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			}
		}
		addNumber(new SkinNumber(ntr[0], 4, 0, NUMBER_SCORE_RATE), 0, 1100, 200, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_SCORE_RATE_AFTERDOT), 0, 1148, 200, 8, 12, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 5, 0, NUMBER_SCORE), 0, 1100, 180, 12, 18, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[1], 5, 0, NUMBER_HIGHSCORE), 0, 1100, 160, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[2], 5, 0, NUMBER_TARGET_SCORE), 0, 1100, 140, 12, 18, 0, 255, 255,
				255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		SkinGraph seek = new SkinGraph(new TextureRegion(st, 0, 0, 8, 8));
		seek.setReferenceID(BARGRAPH_LOAD_PROGRESS);
		seek.setDirection(0);
		setDestination(seek, 0, 210, 440, 860, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 500, 210, 440, 860, 4, 0, 192, 0, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1000, 210, 440, 860, 4, 0, 128, 255, 0, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 1500, 210, 440, 860, 4, 0, 192, 255, 255, 0, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		setDestination(seek, 2000, 210, 440, 860, 4, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 80, 0, 0);
		add(seek);

		lanegroupregion = new Rectangle[] { rect(210, 140, 390, 580), rect(680, 140, 390, 580) };
		// READY
		Texture ready = new Texture("skin/default/ready.png");
		SkinImage ri = new SkinImage(new TextureRegion(ready));
		setDestination(ri, 0, 230, 250, 350, 60, 0, 0, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_READY, 0, 0, 0);
		setDestination(ri, 750, 230, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ri, 1000, 230, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ri);
		ri = new SkinImage(new TextureRegion(ready));
		setDestination(ri, 0, 700, 250, 350, 60, 0, 0, 255, 255, 255, 0, 0, 0, 0, -1, TIMER_READY, 0, 0, 0);
		setDestination(ri, 750, 700, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		setDestination(ri, 1000, 700, 300, 350, 60, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(ri);

		TextureRegion[] wv = new TextureRegion[11];
		TextureRegion[] yv = new TextureRegion[11];
		for (int i = 0; i < 11; i++) {
			wv[i] = new TextureRegion(st, i * 10, 550, 10, 15);
			yv[i] = new TextureRegion(st, i * 10, 565, 10, 15);
		}
		SkinNumber white = new SkinNumber(wv, 3, 0, NUMBER_LANECOVER1);
		white.setOffsety(OFFSET_LANECOVER);
		setDestination(white, 0, 540, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(white);
		SkinNumber yellow = new SkinNumber(yv, 4, 0, NUMBER_DURATION);
		yellow.setOffsety(OFFSET_LANECOVER);
		setDestination(yellow, 0, 740, 720, 10, 15, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0,
				OPTION_LANECOVER1_CHANGING, 0, 0);
		add(yellow);

		addNumber(new SkinNumber(ntr[0], 3, 0, NUMBER_GROOVEGAUGE), 0, 600, 60, 24, 24, 0, 255, 255, 255,
				255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		addNumber(new SkinNumber(ntr[0], 1, 0, NUMBER_GROOVEGAUGE_AFTERDOT), 0, 672, 60, 18, 18, 0, 255,
				255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		// progress
		SkinImage progress = new SkinImage(new TextureRegion(st, 10, 10, 10, 251));
		setDestination(progress, 0, 1075, 140, 12, 540, 0, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		add(progress);
		SkinSlider pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 0, 289, 14, 20) }, 0,0, 2,
				(int) (520 * dh), SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 1073, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, 0, 0, 0, 0);
		add(pseek);
		pseek = new SkinSlider(new TextureRegion[] { new TextureRegion(st, 15, 289, 14, 20) }, 0, 0, 2, (int) (520 * dh),
				SLIDER_MUSIC_PROGRESS);
		setDestination(pseek, 0, 1073, 660, 16, 20, 0, 255, 255, 255, 255, 2, 0, 0, 0, 0, TIMER_ENDOFNOTE_1P,
				0, 0, 0);
		add(pseek);

	}

	public Rectangle[] getLaneregion() {
		return laneregion;
	}

	public Rectangle[] getLaneGroupRegion() {
		return lanegroupregion;
	}

	public void setLaneGroupRegion(Rectangle[] r) {
		lanegroupregion = r;
	}

	public void setLaneregion(Rectangle[] laneregion) {
		this.laneregion = laneregion;
	}

	public void setJudgeregion(int jr) {
		judgeregion = jr;
	}

	public int getJudgeregion() {
		return judgeregion;
	}

	private Rectangle rect(float x, float y, float width, float height) {
		return new Rectangle(x * dw, y * dh, width * dw, height * dh);
	}

	public int getClose() {
		return close;
	}

	public void setClose(int close) {
		this.close = close;
	}

	public int getPlaystart() {
		return playstart;
	}

	public void setPlaystart(int playstart) {
		this.playstart = playstart;
	}

	public SkinImage[] getLine() {
		return line;
	}

	public void setLine(SkinImage[] line) {
		this.line = line;
	}

	public static class JudgeRegion {

		public SkinImage[] judge;
		public SkinNumber[] count;
		public boolean shift;

		public JudgeRegion(SkinImage[] judge, SkinNumber[] count, boolean shift) {
			this.judge = judge;
			this.count = count;
			this.shift = shift;
		}
	}

	public void setBMSPlayer(BMSPlayer player) {
		this.player = player;
	}

	public int getLoadstart() {
		return loadstart;
	}

	public void setLoadstart(int loadstart) {
		this.loadstart = loadstart;
	}

	public int getLoadend() {
		return loadend;
	}

	public void setLoadend(int loadend) {
		this.loadend = loadend;
	}
	
}
