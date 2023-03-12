package pro.devdesign.gameboy

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import pro.devdesign.gameboy.cartridge.CartridgeMeta

class GameBoy(
    private val cartridgeMeta: CartridgeMeta
) : ApplicationAdapter() {

    lateinit var batch: SpriteBatch
    lateinit var logo: Texture

    override fun create() {
        batch = SpriteBatch()
        logo = cartridgeMeta.header().logo().asTexture()
    }

    override fun render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f)
        batch.begin()
        batch.draw(logo, 0f, 0f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }
}
