
/** see ../../../../../../../LICENSE for release rights */
package ws.nzen.pdistillery.md;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/** Changes relevant yaml with markdown to html yaml */
public class MudLog
{
	private static final String cl = "o.";
	static final String mdConfigFlag = "m", yamlConfigFlag = "y",
			filesToProcessFlag = "f", verboseFlag = "v",
			outputFolderPath = "o", helpFlag = "h";
	static final String pluginKey = "pd_left", selfAsPlugin = "mud-log";
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
		180108 next:
		output (echo) provided yaml

		https://github.com/vsch/flexmark-java/tree/master/flexmark-java-samples/src/com/vladsch/flexmark/samples
		*/
		CommandLine userInput = prepCli( prepCliParser(), args );
		MudLog doesStuff = prepDoer( userInput );
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
				new HelpFormatter().printHelp( "PD-MudLog", knowsCliDtd );
			}
		}
		catch ( ParseException pe )
		{
			System.err.println( cl +"pc just using config: couldn't parse input "+ pe );
		}
		return userInput;
	}


	public static MudLog prepDoer( CommandLine userInput )
	{
		MudLog doesStuff;
		if ( userInput != null && userInput.hasOption( verboseFlag ) )
		 {
			boolean wantsLogging = true;
			doesStuff = new MudLog( wantsLogging );
		 }
		else
		{
			 doesStuff = new MudLog();
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
				doesStuff.setYamlPath( userInput
						.getOptionValue( yamlConfigFlag, currentDir ) );
			}
			if ( userInput.hasOption( outputFolderPath ) )
			{
				doesStuff.setOutputPath( userInput
						.getOptionValue( outputFolderPath, currentDir ) );
			}
		}
		doesStuff.adoptConfiguration();
		return doesStuff;
	}


	public MudLog()
	{
		this( false );
	}


	public MudLog( boolean noiseTolerance )
	{
		verbose = noiseTolerance;
	}


	// IMPROVE these are all the same, but it feels wasteful to make an enum just for this
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
			System.err.println( here +"invalid md path "+ ipe );
		}
	}


	public void setYamlPath( String path )
	{
		final String here = cl +"smp ";
		try
		{
			Path place = Paths.get( path );
			if ( place.toFile().isFile() )
			{
				yamlConfig = place;
			}
			else if ( verbose )
			{
				System.err.println( here +"yaml config must be a file" );
			}
		}
		catch ( InvalidPathException ipe )
		{
			System.err.println( here +"invalid yaml path "+ ipe );
		}
	}


	public void setOutputPath( String path )
	{
		final String here = cl +"smp ";
		try
		{
			Path place = Paths.get( path );
			if ( place.toFile().isDirectory() )
			{
				outputFolder = place;
			}
			else if ( verbose )
			{
				System.err.println( here +"output must be a folder" );
			}
		}
		catch ( InvalidPathException ipe )
		{
			System.err.println( here +"invalid out dir "+ ipe );
		}
	}


	/** cleanup after config changes */
	public void adoptConfiguration()
	{
		System.out.println( cl +"ad didnt reify config yet" );
		// configure yaml thing
		// configure md thing
	}


	/** translate the md of the yaml files specified if appropriate */
	public void translateMarkdownOf( String[] paths )
	{
		final String here = cl +"tmo ";
		if ( paths != null )
		{
			YamlReader loadsInfo;
			YamlWriter burysInfo;
			// ASK perhaps move to adoptConfiguration()
			YamlConfig docConfig = new YamlConfig();
			docConfig.writeConfig.setExplicitFirstDocument( true );
			docConfig.writeConfig.setExplicitEndDocument( true );
			for ( String path : paths )
			{
				System.out.println( cl +"tmo trans "+ path );
				try ( FileReader charLoader = new FileReader( path );
						FileWriter charDump = new FileWriter(
							outputFolder.toString() + File.separator
							+ Paths.get( path ).getFileName() ) )
				{
					if ( verbose )
					{
						System.out.println( here +"new file is "
								+ outputFolder.toString() + File.separator
								+ Paths.get( path ).getFileName() );
					}
					loadsInfo = new YamlReader( charLoader );
					burysInfo = new YamlWriter( charDump );
					Object document;
					Map docAttributes;
					boolean translateMd = false;
					while ( true )
					{
						document = loadsInfo.read();
						if ( document == null )
						{
							break; // end of file
						}
						else
						{
							System.out.println( document );
							if ( document instanceof Map )
							{
								docAttributes = (Map)document;
								if ( docAttributes.containsKey( pluginKey ) )
								{
									List plugins = (List)(docAttributes.get( pluginKey ));
									// IMPROVE handle class cast ex
									int indOfSelf = plugins.indexOf( selfAsPlugin );
									if ( indOfSelf >= 0 )
									{
										plugins.remove( indOfSelf );
										docAttributes.put( pluginKey, plugins );
										translateMd = true;
										
									}
								}
								burysInfo.write( document );
							}
							else if ( translateMd )
							{
								// check for content thing or next doc
								if ( document instanceof String )
								{
									// this is our markdown
									String content = (String)document;
									content = content.toUpperCase();
									System.out.println( here +"didnt translate yet" );
									burysInfo.write( content );
								}
								else
								{
									burysInfo.write( document );
								}
							}
							else
							{
								burysInfo.write( document );
							}
						}
					}
					loadsInfo.close();
					burysInfo.close();
				}
				catch ( FileNotFoundException | InvalidPathException ie )
				{
					System.err.println( here +"invalid path "+ ie );
				}
				catch ( IOException ie )
				{
					System.err.println( here +"file i/o problem "+ ie );
				}
			}
		}
	}

}




























