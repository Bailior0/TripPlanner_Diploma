package hu.bme.aut.onlab.tripplanner.presenters

import android.content.Context
import android.graphics.Bitmap
import co.zsmb.rainbowcake.test.base.PresenterTest
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.onlab.tripplanner.domain.TripsInteractor
import hu.bme.aut.onlab.tripplanner.ui.list.pages.identifier.IdentifierPresenter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.tensorflow.lite.support.label.Category

@OptIn(ExperimentalCoroutinesApi::class)
class IdentifierPresenterTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)

    private lateinit var tripsInteractor: TripsInteractor
    private lateinit var presenter: IdentifierPresenter

    private val context: Context = mockk(relaxed = true)
    private val bitmap: Bitmap = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        tripsInteractor = mockk()
        presenter = IdentifierPresenter(tripsInteractor)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `identify delegates to TripsInteractor`() = runTest {
        val category = Category("Eiffel Tower", 0.97f)
        coEvery { tripsInteractor.identify(bitmap, context) } returns category

        val result = presenter.identify(bitmap, context)

        assertThat(result).isEqualTo(category)
        coVerify(exactly = 1) { tripsInteractor.identify(bitmap, context) }
    }
}
