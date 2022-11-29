package hu.bme.aut.onlab.tripplanner.presenters

import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.domain.AuthInteractor
import hu.bme.aut.onlab.tripplanner.domain.ShareInteractor
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.SharePresenter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
    fun shareGetItemsTest() = runBlocking {
        val flowItems = flowOf(listOf(MOCK_POST))
        coEvery { shareInteractor.getItems("Paris") } returns flowItems

        val items = presenter.getItems("Paris")

        assertThat(items).isEqualTo(flowItems)
    }

    @Test
    fun shareGetItemsOnceTest() = runBlocking {
        coEvery { shareInteractor.getItemsOnce("Paris") } returns listOf(MOCK_POST)

        val items = presenter.getItemsOnce("Paris")

        assertThat(items).isEqualTo(listOf(MOCK_POST))
    }

    @Test
    fun getCurrentUser() = runBlocking {
        coEvery { authInteractor.getCurrentUser() } returns "0"

        val user = presenter.getCurrentUser()

        assertThat(user).isEqualTo("0")
    }
}