/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.changedetection.rules

import org.gradle.api.internal.changedetection.state.FileCollectionSnapshot
import org.gradle.api.internal.changedetection.state.OutputFilesSnapshotter
import org.gradle.api.internal.changedetection.state.TaskHistoryRepository
import org.gradle.api.internal.changedetection.state.ValueSnapshotter
import org.gradle.api.internal.file.FileCollectionFactory
import org.gradle.api.snapshotting.internal.GenericSnapshotters
import org.gradle.internal.classloader.ClassLoaderHierarchyHasher
import spock.lang.Subject

@Subject(TaskUpToDateState)
class TaskUpToDateStateTest extends AbstractTaskStateChangesTest {
    private TaskHistoryRepository.History stubHistory
    private OutputFilesSnapshotter stubOutputFileSnapshotter
    private FileCollectionFactory fileCollectionFactory = Mock(FileCollectionFactory)
    private classLoaderHierarchyHasher = Mock(ClassLoaderHierarchyHasher)

    def setup() {
        this.stubHistory = Stub(TaskHistoryRepository.History)
        this.stubOutputFileSnapshotter = Stub(OutputFilesSnapshotter)
    }

    def "constructor invokes snapshots" () {
        setup:
        def stubSnapshot = Stub(FileCollectionSnapshot)
        def mockOutputFileSnapshotter = Mock(OutputFilesSnapshotter)

        when:
        new TaskUpToDateState(stubTask, stubHistory, mockOutputFileSnapshotter, fileCollectionFactory, classLoaderHierarchyHasher, new ValueSnapshotter(classLoaderHierarchyHasher), mockFileSystemSnapshotter)

        then:
        noExceptionThrown()
        1 * mockInputs.getProperties() >> [:]
        1 * mockInputs.getFileProperties() >> fileProperties(prop: "a")
        1 * mockOutputs.getFileProperties() >> fileProperties(out: "b")
        (1.._) * mockSnapshottingConfiguration.createSnapshotter(GenericSnapshotters.Absolute) >> mockResourceSnapshotter
        (1.._) * mockFileSystemSnapshotter.snapshotFileCollection(_, mockResourceSnapshotter) >> stubSnapshot
    }
}
