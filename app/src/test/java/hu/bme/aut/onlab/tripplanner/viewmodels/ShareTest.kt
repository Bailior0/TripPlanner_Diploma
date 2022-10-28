package hu.bme.aut.onlab.tripplanner.viewmodels

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import kotlinx.coroutines.test.advanceUntilIdle
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.Loading
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.ShareContent
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.SharePresenter
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.ShareViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShareViewModelTest : ViewModelTest() {
    private lateinit var sharePresenter: SharePresenter
    private lateinit var viewModel: ShareViewModel

    @Before
    fun initEach() {
        sharePresenter = mockk()
        viewModel = ShareViewModel(sharePresenter)
    }

    companion object {
        private val MOCK_POST = SharedData(
            nickname  = "Béla",
            title = "Szép",
            body = "Nagyon szép volt",
        )
    }

    @Test
    fun shareLoadedTest() = runTest {
        coEvery { sharePresenter.getItems("Paris") } returns flowOf(listOf(MOCK_POST))
        coEvery { sharePresenter.getCurrentUser() } returns ("0")

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.setShare("Paris")
            advanceUntilIdle()
            stateObserver.assertObserved(Loading, ShareContent(), ShareContent(listOf(MOCK_POST), "0", false))
        }
    }

    @Test
    fun shareEditedTest() = runTest {
        coEvery { sharePresenter.getItemsOnce("Paris") } returns listOf(MOCK_POST)
        coEvery { sharePresenter.editPost("Paris", MOCK_POST) } returns Unit
        coEvery { sharePresenter.getCurrentUser() } returns ("0")

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.editPost("Paris", MOCK_POST)
        coVerify(exactly = 1) { sharePresenter.getCurrentUser() }
        coVerify(exactly = 1) { sharePresenter.editPost("Paris", MOCK_POST) }
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(ShareContent(listOf(MOCK_POST), "0"))
        }
    }

    @Test
    fun shareLikedTest() = runTest {
        coEvery { sharePresenter.getItemsOnce("Paris") } returns listOf(MOCK_POST)
        coEvery { sharePresenter.likePost("Paris", MOCK_POST) } returns Unit
        coEvery { sharePresenter.getCurrentUser() } returns ("0")

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.likePost("Paris", MOCK_POST)
        coVerify(exactly = 1) { sharePresenter.getCurrentUser() }
        coVerify(exactly = 1) { sharePresenter.likePost("Paris", MOCK_POST) }
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(ShareContent(listOf(MOCK_POST), "0"))
        }
    }
}