/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.services.backend.compiler.impl;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kie.workbench.common.services.backend.compiler.nio.decorators.JGITCompilerBeforeDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.java.nio.fs.jgit.JGitFileSystem;

/***
 * Class used to provides JGit functionalities to the JGIT compiler decorators
 */
public class JGitUtils {

    private static final Logger logger = LoggerFactory.getLogger(JGITCompilerBeforeDecorator.class);
    private static String REMOTE = "origin";
    private static String TEMP = System.getProperty("java.io.tmpdir") + File.separatorChar + "maven/";

    public static Boolean applyBefore(final Git git) {
        Boolean result = Boolean.FALSE;
        try {
            PullCommand pc = git.pull().setRemote(REMOTE).setRebase(Boolean.TRUE);
            PullResult pullRes = pc.call();
            RebaseResult rr = pullRes.getRebaseResult();

            if (rr.getStatus().equals(RebaseResult.Status.UP_TO_DATE) || rr.getStatus().equals(RebaseResult.Status.FAST_FORWARD)) {
                result = Boolean.TRUE;
            }
            if (rr.getStatus().equals(RebaseResult.Status.UNCOMMITTED_CHANGES)) {
                PullResult pr = git.pull().call();
                if (pr.isSuccessful()) {
                    result = Boolean.TRUE;
                } else {
                    result = Boolean.FALSE;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public static Git tempClone(final JGitFileSystem fs,
                                final String uuid) {
        try {
            return Git.cloneRepository()
                    .setURI(fs.getGit().getRepository().getDirectory().toURI().toString())
                    .setDirectory(new File(TEMP + uuid,
                                           fs.getGit().getRepository().getDirectory().getName().replaceFirst("\\.git", "")))
                    .setBare(false)
                    .setCloneAllBranches(true)
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }
}
