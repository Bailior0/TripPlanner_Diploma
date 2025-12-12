package hu.bme.aut.onlab.tripplanner.viewmodels

import android.content.Context
import android.graphics.Bitmap
import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierContent
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierPresenter
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierViewModel
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.Loading
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.tensorflow.lite.support.label.Category

@OptIn(ExperimentalCoroutinesApi::class)
class IdentifierViewModelTest : ViewModelTest() {

    private lateinit var presenter: IdentifierPresenter
    private lateinit var viewModel: IdentifierViewModel

    private val context: Context = mockk(relaxed = true)
    private val bitmap: Bitmap = mockk(relaxed = true)
    private val prediction = Category("Eiffel Tower", 0.95f)

    @Before
    fun setup() {
        presenter = mockk()
        viewModel = IdentifierViewModel(presenter)
    }

    @Test
    fun `load emits loading then empty content`() = runTest {
        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.load()
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                IdentifierContent(loading = true),
                IdentifierContent(prediction = null, loading = false)
            )
        }
    }

    @Test
    fun `identify emits loading then prediction with image`() = runTest {
        coEvery { presenter.identify(bitmap, context) } returns prediction

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.identify(bitmap, context)
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                IdentifierContent(loading = true),
                IdentifierContent(prediction = prediction, image = bitmap, loading = false)
            )
        }
    }
}
