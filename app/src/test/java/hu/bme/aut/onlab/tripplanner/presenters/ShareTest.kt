package hu.bme.aut.onlab.tripplanner.presenters

import android.graphics.Bitmap
import co.zsmb.rainbowcake.test.base.PresenterTest
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import hu.bme.aut.onlab.tripplanner.domain.ShareInteractor
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.SharePresenter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharePresenterTest : PresenterTest() {

    private lateinit var shareInteractor: ShareInteractor
    private lateinit var authInteractor: AuthInteractor
    private lateinit var presenter: SharePresenter

    @Before
    fun initEach() {
        shareInteractor = mockk()
        authInteractor = mockk()
        presenter = SharePresenter(shareInteractor, authInteractor)
    }

    companion object {
        private val MOCK_POST = SharedData(
            nickname = "Béla",
            title = "Szép",
            body = "Nagyon szép volt",
        )
    }

    @Test
    fun `getItems returns the flow from ShareInteractor`() = runBlocking {
        val flowItems = flowOf(listOf(MOCK_POST))
        coEvery { shareInteractor.getItems("Paris") } returns flowItems

        val result = presenter.getItems("Paris")

        assertEquals(flowItems, result)
    }

    @Test
    fun `getItemsOnce returns the list from ShareInteractor`() = runBlocking {
        coEvery { shareInteractor.getItemsOnce("Paris") } returns listOf(MOCK_POST)

        val result = presenter.getItemsOnce("Paris")

        assertEquals(listOf(MOCK_POST), result)
    }

    @Test
    fun `uploadPost delegates all params to ShareInteractor`() = runBlocking {
        val image: Bitmap? = mockk(relaxed = true)
        coEvery { shareInteractor.uploadPost("Paris", "Nick", "Title", "Body", image) } returns Unit

        presenter.uploadPost("Paris", "Nick", "Title", "Body", image)

        coVerify(exactly = 1) {
            shareInteractor.uploadPost("Paris", "Nick", "Title", "Body", image)
        }
    }

    @Test
    fun `editPost delegates to ShareInteractor`() = runBlocking {
        val image: Bitmap? = mockk(relaxed = true)
        coEvery { shareInteractor.editPost(MOCK_POST, image) } returns Unit

        presenter.editPost(MOCK_POST, image)

        coVerify(exactly = 1) { shareInteractor.editPost(MOCK_POST, image) }
    }

    @Test
    fun `deletePost delegates to ShareInteractor`() = runBlocking {
        coEvery { shareInteractor.deletePost(MOCK_POST) } returns Unit

        presenter.deletePost(MOCK_POST)

        coVerify(exactly = 1) { shareInteractor.deletePost(MOCK_POST) }
    }

    @Test
    fun `likePost delegates to ShareInteractor`() = runBlocking {
        coEvery { shareInteractor.likePost(MOCK_POST) } returns Unit

        presenter.likePost(MOCK_POST)

        coVerify(exactly = 1) { shareInteractor.likePost(MOCK_POST) }
    }

    @Test
    fun `getCurrentUser returns the value from AuthInteractor`() = runBlocking {
        coEvery { authInteractor.getCurrentUser() } returns "user-id"

        val result = presenter.getCurrentUser()

        assertEquals("user-id", result)
    }
}
