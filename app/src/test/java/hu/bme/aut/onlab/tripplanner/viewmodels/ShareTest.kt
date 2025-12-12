package hu.bme.aut.onlab.tripplanner.viewmodels

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.onlab.tripplanner.data.network.model.SharedData
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.Loading
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.ShareContent
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.SharePresenter
import hu.bme.aut.onlab.tripplanner.ui.details.pages.share.ShareViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
            nickname = "Béla",
            title = "Szép",
            body = "Nagyon szép volt"
        )
    }

    @Test
    fun `setShare loads posts and updates state`() = runTest {
        coEvery { sharePresenter.getItems("Paris") } returns flowOf(listOf(MOCK_POST))
        coEvery { sharePresenter.getCurrentUser() } returns "0"

        // initial state
        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.setShare("Paris")
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                ShareContent(isLoading = true),
                ShareContent(
                    list = listOf(MOCK_POST),
                    currentUser = "0",
                    isLoading = false
                )
            )
        }
    }

    @Test
    fun `editPost delegates to presenter and refreshes list`() = runTest {
        coEvery { sharePresenter.getCurrentUser() } returns "0"
        coEvery { sharePresenter.editPost(MOCK_POST, null) } returns Unit
        coEvery { sharePresenter.getItemsOnce("Paris") } returns listOf(MOCK_POST)

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.editPost("Paris", MOCK_POST, null)

        coVerify(exactly = 1) { sharePresenter.getCurrentUser() }
        coVerify(exactly = 1) { sharePresenter.editPost(MOCK_POST, null) }
        coVerify(exactly = 1) { sharePresenter.getItemsOnce("Paris") }

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                ShareContent(
                    list = listOf(MOCK_POST),
                    currentUser = "0",
                    isLoading = true
                )
            )
        }
    }

    @Test
    fun `likePost delegates to presenter and refreshes list`() = runTest {
        coEvery { sharePresenter.getCurrentUser() } returns "0"
        coEvery { sharePresenter.likePost(MOCK_POST) } returns Unit
        coEvery { sharePresenter.getItemsOnce("Paris") } returns listOf(MOCK_POST)

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }

        viewModel.likePost("Paris", MOCK_POST)

        coVerify(exactly = 1) { sharePresenter.getCurrentUser() }
        coVerify(exactly = 1) { sharePresenter.likePost(MOCK_POST) }
        coVerify(exactly = 1) { sharePresenter.getItemsOnce("Paris") }

        viewModel.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                ShareContent(
                    list = listOf(MOCK_POST),
                    currentUser = "0",
                    isLoading = true
                )
            )
        }
    }
}
