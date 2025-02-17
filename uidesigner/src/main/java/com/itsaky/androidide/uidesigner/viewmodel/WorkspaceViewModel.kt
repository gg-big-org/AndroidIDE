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

package com.itsaky.androidide.uidesigner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.uidesigner.models.UiAttribute
import com.itsaky.androidide.uidesigner.undo.AttrAddedAction
import com.itsaky.androidide.uidesigner.undo.AttrUpdatedAction
import com.itsaky.androidide.uidesigner.undo.UndoManager
import java.io.File

internal class WorkspaceViewModel : ViewModel() {
  internal val _drawerOpened = MutableLiveData(false)
  internal val _errText = MutableLiveData("")
  internal val _workspaceScreen = MutableLiveData(SCREEN_WORKSPACE)
  internal val _view = MutableLiveData<IView>(null)
  internal val _selectedAttr = MutableLiveData<IAttribute>(null)
  internal val _addAttrMode = MutableLiveData(false)
  internal val _undoManager = MutableLiveData(UndoManager())
  private val _file = MutableLiveData<File>()

  companion object {
    const val SCREEN_WORKSPACE = 0
    const val SCREEN_ERROR = 1
  }

  val undoManager: UndoManager
    get() = this._undoManager.value!!

  var file: File
    get() = _file.value!!
    set(value) {
      _file.value = value
    }

  var drawerOpened: Boolean
    get() = this._drawerOpened.value!!
    set(value) {
      this._drawerOpened.value = value
    }

  var errText: String
    get() = _errText.value ?: ""
    set(value) {
      _errText.value = value
      workspaceScreen = SCREEN_ERROR
    }

  var workspaceScreen: Int
    get() = _workspaceScreen.value ?: SCREEN_WORKSPACE
    set(value) {
      _workspaceScreen.value = value
    }

  var view: IView?
    get() = this._view.value
    set(value) {
      this._view.value = value
    }

  var selectedAttr: IAttribute?
    get() = this._selectedAttr.value
    set(value) {
      this._selectedAttr.value = value
    }

  var addAttrMode: Boolean
    get() = this._addAttrMode.value ?: false
    set(value) {
      this._addAttrMode.value = value
    }

  fun notifyAttrUpdated() {
    val attr = this.selectedAttr ?: return
    val view = this.view ?: return
    val undoManager = this.undoManager

    if (addAttrMode) {
      if (attr.value.isBlank()) {
        view.removeAttribute(attr)
        return
      }
      
      undoManager.push(AttrAddedAction(view, attr as UiAttribute))
      return
    }

    val existing = view.findAttribute(attr.namespace.uri, attr.name)

    if (existing !is UiAttribute || existing.value == attr.value) {
      // value of the attribute is same as before
      return
    }

    undoManager.push(
      AttrUpdatedAction(view, existing.copyAttr(view = view) as UiAttribute, attr.value)
    )
    this.selectedAttr = null
  }
}
