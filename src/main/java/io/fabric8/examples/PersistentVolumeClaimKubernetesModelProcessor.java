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

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.generator.annotation.KubernetesModelProcessor;

import javax.inject.Named;
import java.util.HashMap;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@KubernetesModelProcessor
public class PersistentVolumeClaimKubernetesModelProcessor {

    public void on(KubernetesListBuilder builder){
        builder.addNewPersistentVolumeClaimItem()
                .withNewMetadata()
                  .withName("typesafe-dsl-pv")
                  .addToLabels("provider", "fabric8")
                  .addToLabels("project", "typesafe-kubernetes-dsl")
                  .addToLabels("group", "demo")
                .endMetadata()
                .withNewSpec()
                  .withAccessModes("ReadWriteOnce")
                  .withResources(getResourceRequirement())
                .endSpec()
                .endPersistentVolumeClaimItem()
                .build();
    }

    private ResourceRequirements getResourceRequirement() {
        ResourceRequirements rc = new ResourceRequirements();

        Quantity claimSize = new Quantity("100Ki");
        HashMap<String, Quantity> requests = new HashMap<>();
        requests.put("storage", claimSize);
        rc.setRequests(requests);
        return rc;
    }

    public void withPodTemplate(PodTemplateSpecBuilder builder) {
        builder.withSpec(builder.getSpec()).editSpec()
                .addNewVolume()
                .withName("typesafe-kubernetes-dsl-volume")
                .withPersistentVolumeClaim(getPersistentVolumeClaimSource())
                .endVolume()
                .endSpec().build();
    }

    private PersistentVolumeClaimVolumeSource getPersistentVolumeClaimSource() {
        PersistentVolumeClaimVolumeSource rc = new PersistentVolumeClaimVolumeSource("typesafe-kubernetes-dsl-pvc", false);
        return rc;
    }

    @Named("typesafe-kubernetes-dsl")
    public void withVolumeMounts(ContainerBuilder builder) {
        builder.withVolumeMounts(new VolumeMount("/deployments/target/placeorder", "typesafe-kubernetes-dsl-volume", false))
                .build();
    }
}
