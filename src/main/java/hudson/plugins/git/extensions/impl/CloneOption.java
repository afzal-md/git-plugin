package hudson.plugins.git.extensions.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.plugins.git.GitException;
import hudson.plugins.git.GitSCM;
import hudson.plugins.git.extensions.GitClientType;
import hudson.plugins.git.extensions.GitSCMExtension;
import hudson.plugins.git.extensions.GitSCMExtensionDescriptor;
import org.jenkinsci.plugins.gitclient.CloneCommand;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public class CloneOption extends GitSCMExtension {
    private boolean shallow;
    private String reference;

    @DataBoundConstructor
    public CloneOption(boolean shallow, String reference) {
        this.shallow = shallow;
        this.reference = reference;
    }

    public boolean isShallow() {
        return shallow;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public void decorateCloneCommand(GitSCM scm, AbstractBuild<?, ?> build, GitClient git, BuildListener listener, CloneCommand cmd) throws IOException, InterruptedException, GitException {
        if (shallow) {
            listener.getLogger().println("Using shallow clone");
            cmd.shallow();
        }
        cmd.reference(build.getEnvironment(listener).expand(reference));
    }

    @Override
    public GitClientType getRequiredClient() {
        return GitClientType.GITCLI;
    }

    @Extension
    public static class DescriptorImpl extends GitSCMExtensionDescriptor {
        @Override
        public String getDisplayName() {
            return "Advanced clone behaviours";
        }
    }

}
