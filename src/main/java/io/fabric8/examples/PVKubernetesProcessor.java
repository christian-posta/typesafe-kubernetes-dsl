/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.examples;

import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.generator.annotation.KubernetesProvider;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
public class PVKubernetesProcessor {


    @KubernetesProvider("typesafe-kubernetes-dsl-pv.yml")
    public KubernetesList buildList() {
        return new KubernetesListBuilder().addNewPersistentVolumeItem()
                .withNewMetadata()
                    .withName("typesafe-kubernetes-dsl-pv")
                    .addToLabels("provider", "fabric8")
                    .addToLabels("project", "typesafe-kubernetes-dsl")
                    .addToLabels("group", "demo")
                .endMetadata()
                .withNewSpec()
                    .addToCapacity("storage", new Quantity("100Ki"))
                    .addToAccessModes("ReadWriteOnce")
                    .withHostPath(new HostPathVolumeSource("/home/vagrant/camel"))
                .endSpec()
                .endPersistentVolumeItem()
                .build();
    }
}
