/*
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.service.assets

import java.io.File

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.waz.utils.{Cleanup, Managed}

object MetaDataRetriever {

  implicit lazy val RetrieverCleanup = new Cleanup[MediaMetadataRetriever] {
    override def apply(a: MediaMetadataRetriever): Unit = a.release()
  }

  def apply[A](body: MediaMetadataRetriever => A): A = Managed(new MediaMetadataRetriever).acquire { body }

  def apply[A](file: File)(f: MediaMetadataRetriever => A): A =
    apply { retriever =>
      retriever.setDataSource(file.getAbsolutePath)
      f(retriever)
    }

  def apply[A](context: Context, uri: Uri)(f: MediaMetadataRetriever => A): A =
    apply { retriever =>
      retriever.setDataSource(context, uri)
      f(retriever)
    }
}
