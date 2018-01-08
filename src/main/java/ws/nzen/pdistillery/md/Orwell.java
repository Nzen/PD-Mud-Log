
/** see ../../../../../../../LICENSE for release rights */
package ws.nzen.pdistillery.md;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/** Changes relevant yaml with markdown to html yaml */
public class Orwell
{
	private static final String cl = "o.";
	static final String mdConfigFlag = "m", yamlConfigFlag = "y",
			filesToProcessFlag = "f", verboseFlag = "v",
			outputFolderPath = "o", helpFlag = "h";
	protected boolean verbose;
	protected Path markdownConfig = null;
	protected Path yamlConfig = null;
	protected Path outputFolder = null;

	public static void main( String[] args )
	{
		/*
		pick out the md config from the args (maybe use a c cli)
		send the rest of args to become a list of paths
		*
		prep the md thing
		*
		for each file
		get yaml thing of file
		(if latest plugin is md)
		provide contents to md thing
		strip md from plugin list
		rewrite in the current folder
		==
		X Show the config path vs the list of files
		Configure yaml?
		Show the yaml of a file
		Ensure appropriate place exists
		Rewrite the unchanged file in the appropriate place
		Configure md
		Show md transformed yaml content
		Rewrite the transformed file in the appropriate place
		==
		180101 next:
		honor output folder arg
		honor the yaml file arg
		-
		write a two document yaml
		init a yaml reader
		provide a file
		show the contents

		https://github.com/vsch/flexmark-java/tree/master/flexmark-java-samples/src/com/vladsch/flexmark/samples
		*/
		CommandLine userInput = prepCli( prepCliParser(), args );
		Orwell doesStuff = prepDoer( userInput );
		if ( userInput != null )
		{
			doesStuff.translateMarkdownOf( userInput
					.getOptionValues( filesToProcessFlag ) );
		}
	}


	/** fills options with our cli flags and text */
	public static Options prepCliParser()
	{
		Options knowsCliDtd = new Options();
		final boolean needsEmbelishment = true;
		knowsCliDtd.addOption( mdConfigFlag, needsEmbelishment, "markdown config path"
				+ " (ex C:\\Program Files\\apache\\tomcat.txt)" );
		knowsCliDtd.addOption( yamlConfigFlag, needsEmbelishment, "yaml config path"
				+ " (ex /home/theusername/tmp/ff.json)" );
		knowsCliDtd.addOption( outputFolderPath, needsEmbelishment,
				"path of folder to write results" );
		knowsCliDtd.addOption( filesToProcessFlag, needsEmbelishment,
				"paths of files to process" );
		knowsCliDtd.addOption( verboseFlag, ! needsEmbelishment,
				"show debug information" );
		knowsCliDtd.addOption( helpFlag, "show arg flags" );
		final int numberOfFilesToAcceptFromFolder
				= org.apache.commons.cli.Option.UNLIMITED_VALUES;
		knowsCliDtd.getOption( filesToProcessFlag )
				.setArgs( numberOfFilesToAcceptFromFolder );
		return knowsCliDtd;
	}


	/** Parses the actual input and shows help, if requested */
	public static CommandLine prepCli(
			Options knowsCliDtd, String[] args )
	{
		CommandLineParser cliRegex = new DefaultParser();
		CommandLine userInput = null;
		try
		{
			userInput = cliRegex.parse( knowsCliDtd, args );
			if ( userInput.hasOption( helpFlag ) )
			{
				new HelpFormatter().printHelp( "PD-Orwell", knowsCliDtd );
			}
		}
		catch ( ParseException pe )
		{
			System.err.println( cl +"pc just using config: couldn't parse input "+ pe );
		}
		return userInput;
	}


	public static Orwell prepDoer( CommandLine userInput )
	{
		Orwell doesStuff;
		if ( userInput != null && userInput.hasOption( verboseFlag ) )
		 {
			boolean wantsLogging = true;
			doesStuff = new Orwell( wantsLogging );
		 }
		else
		{
			 doesStuff = new Orwell();
		}
		if ( userInput != null )
		{
			final String currentDir = "";
			if ( userInput.hasOption( mdConfigFlag ) )
			{
				doesStuff.setMarkdownPath( userInput
						.getOptionValue( mdConfigFlag, currentDir ) );
			}
			if ( userInput.hasOption( yamlConfigFlag ) )
			{
				System.out.println( cl +"pd didnt save yaml config yet" );
			}
			if ( userInput.hasOption( outputFolderPath ) )
			{
				System.out.println( cl +"pd didnt save yaml config yet" );
			}
		}
		doesStuff.adoptConfiguration();
		return doesStuff;
	}


	public Orwell()
	{
		this( false );
	}


	public Orwell( boolean noiseTolerance )
	{
		verbose = noiseTolerance;
	}


	public void setMarkdownPath( String path )
	{
		final String here = cl +"smp ";
		try
		{
			Path place = Paths.get( path );
			if ( place.toFile().isFile() )
			{
				markdownConfig = place;
			}
			else if ( verbose )
			{
				System.err.println( here +"md config must be a file" );
			}
		}
		catch ( InvalidPathException ipe )
		{
			System.err.println( here +"invalid md dir "+ ipe );
		}
	}
	//yml output


	/** cleanup after config changes */
	public void adoptConfiguration()
	{
		// configure yaml thing
		// configure md thing
		System.out.println( cl +"ad didnt reify config yet" );
	}


	/** translate the md of the yaml files specified if appropriate */
	public void translateMarkdownOf( String[] paths )
	{
		System.out.println( cl +"tmo didnt translate yet" );
		if ( paths != null )
		{
			for ( String path : paths )
			{
				System.out.println( cl +"tmo trans "+ path );
			}
		}
	}

}




























