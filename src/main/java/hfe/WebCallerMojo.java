package hfe;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


@Mojo(name = "web", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, threadSafe = true)
public class WebCallerMojo extends AbstractMojo {

    /**
     * The Maven Project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "git")
    private String prefix;

    @Parameter(defaultValue = "any")
    private String githash;

    @Parameter()
    private String url;


    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("WebCallerMojo: prefix: " + prefix);
        getLog().info("WebCallerMojo: url: " + url);
        getLog().info("WebCallerMojo: githash: " + githash);

        Client client = JerseyClientBuilder.createClient();
        WebTarget target = client.target(url).path(githash);
        Response response = target.request().get();
        String sequentialNumber = response.readEntity(String.class);
        getLog().info("WebCallerMojo: sequentialNumber: " + sequentialNumber);
        project.getProperties().put(prefix + ".sequentialNumber", sequentialNumber);
    }
}
