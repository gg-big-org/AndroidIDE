/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.inflater

import android.view.*
import android.widget.*
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.inflater.internal.ViewAdapterIndex
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
class AdapterIndexText {

  @Test
  fun `verify instances of adapters`() {
    viewToAdapter.forEach {
      ViewAdapterIndex.getAdapter(it.key.qualifiedName).apply {
        assertThat(this).isNotNull()
        assertThat(this).isInstanceOf(it.value.java)
      }
    }

    abstractViews.forEach { assertThat(ViewAdapterIndex.getAdapter(it.qualifiedName)).isNull() }
  }
}
