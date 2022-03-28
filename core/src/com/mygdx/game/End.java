package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class End implements Screen{
    final Drop game;
    TextureRegion backgroundTexture;
    Music backgroundMusic;
    OrthographicCamera camera;
    SpriteBatch batch;

    public End(final Drop gam) {
        game = gam;

        backgroundTexture = new TextureRegion(new Texture("fonn.jpg"), 0, 0, 800, 480);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Очищение экрана
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Отрисовка текстур
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        game.batch.draw(backgroundTexture, 0, Gdx.graphics.getHeight());
        game.font.draw(game.batch, "Amazing! You win! ", 100, 150); //Надпись
        game.font.draw(game.batch, "The end", 100, 100);            //Надпись
        game.batch.end();   //Конец отрисовки

        camera.update();    //Обновление положения камеры
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();

    }

}

