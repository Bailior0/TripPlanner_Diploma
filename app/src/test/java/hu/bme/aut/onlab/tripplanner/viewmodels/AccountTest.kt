package hu.bme.aut.onlab.tripplanner.viewmodels

import android.content.Context
import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.google.common.truth.Truth.assertThat
import hu.bme.aut.onlab.tripplanner.data.disk.model.User
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.AccountContent
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.AccountPresenter
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.AccountViewModel
import hu.bme.aut.onlab.tripplanner.ui.list.pages.account.Loading
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest : ViewModelTest() {

    private lateinit var presenter: AccountPresenter
    private lateinit var viewModel: AccountViewModel

    private val context: Context = mockk(relaxed = true)

    private val mockUser = User(
        name = "Béla",
        email = "bela@example.com"
    )

    @Before
    fun setup() {
        presenter = mockk()
        viewModel = AccountViewModel(presenter)
    }

    @Test
    fun `load emits Loading then AccountContent with user`() = runTest {
        coEvery { presenter.getUser() } returns mockUser

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.load()
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                AccountContent(loading = true),
                AccountContent(loading = false, user = mockUser)
            )
        }
    }

    @Test
    fun `load with null user emits only Loading and loading true`() = runTest {
        coEvery { presenter.getUser() } returns null

        viewModel.observeStateAndEvents { stateObserver, _ ->
            viewModel.load()
            advanceUntilIdle()

            stateObserver.assertObserved(
                Loading,
                AccountContent(loading = true)
            )
        }
    }

    @Test
    fun `getUserEmail delegates to presenter`() = runTest {
        coEvery { presenter.getUserEmail() } returns "mail@example.com"

        viewModel.getUserEmail()
        advanceUntilIdle()

        coVerify(exactly = 1) { presenter.getUserEmail() }
    }

    @Test
    fun `signOut delegates to presenter`() = runTest {
        coEvery { presenter.signOut() } returns Unit

        viewModel.signOut()
        advanceUntilIdle()

        coVerify(exactly = 1) { presenter.signOut() }
    }

    @Test
    fun `changePassword delegates to presenter`() = runTest {
        coEvery { presenter.changePassword() } returns Unit

        viewModel.changePassword()
        advanceUntilIdle()

        coVerify(exactly = 1) { presenter.changePassword() }
    }

    @Test
    fun `changeEmail delegates to presenter`() = runTest {
        coEvery { presenter.changeEmail(context, "pass", "new@mail.com") } returns Unit

        viewModel.changeEmail(context, "pass", "new@mail.com")
        advanceUntilIdle()

        coVerify(exactly = 1) { presenter.changeEmail(context, "pass", "new@mail.com") }
    }
}
