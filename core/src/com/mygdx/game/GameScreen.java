package com.mygdx.game;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final Drop game;
    TextureRegion background;
    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;

    public GameScreen(final Drop gam) {
        this.game = gam;

        // Загрузка изображений ведра и яйца
        dropImage = new Texture(Gdx.files.internal("egg.png"));
        bucketImage = new Texture(Gdx.files.internal("basket.png"));
        background = new TextureRegion(new Texture("fonn.jpg"), 0, 0, 800, 480);
        // Загрузка музыки
        dropSound = Gdx.audio.newSound(Gdx.files.internal("Wolf.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
        rainMusic.setLooping(true); //Бесконечное проигрывание theme.mp3

        // создание объекта камеры
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); //Установка координат камеры

        // Создание контейнера, в который будет помещена корзина
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // положение корзины по оси x
        bucket.y = 20; // положение корзины по оси y
        //размеры корзины
        bucket.width = 64f;
        bucket.height = 64f;

        // создание списка яиц (чтобы отрисовывать движение яиц)
        raindrops = new Array<Rectangle>();
        spawnRaindrop();

    }

    //Генерация яйца
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64); //Положение по оси x
        raindrop.y = 480;                           //Положение по оси x

        //Размер
        raindrop.width = 64;
        raindrop.height = 64;

        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        //Цвет фона
        ScreenUtils.clear(1, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Обновление камеры
        camera.update();

        //??
        game.batch.setProjectionMatrix(camera.combined);

        //Отрисовка яйца и ведра
        game.batch.begin();
        game.batch.draw(background, 0, 0, background.getRegionWidth(),background.getRegionHeight());
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        game.font.setColor(0,0,0,0);
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();

        // обработка нажатия на экран
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }

        //Стрелка влево
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        //Стрелка вправо
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // Чтобы корзина не улетела за границы экрана
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // нужно ли новое яйцо рисовать?
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        //Движение яйца
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {    //Пока есть куда двигаться яйцу
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();    //Изменение координаты y
            if (raindrop.y + 64 < 0)
                iter.remove();
            //Если яйцо попало в корзину
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
            if (dropsGathered >= 5) {
                game.setScreen(new End(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        rainMusic.play();   //Запуск музыки
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        //Уничтожение объектов
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }

}