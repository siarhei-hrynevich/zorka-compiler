package com.flex.compiler;

import org.apache.commons.cli.*;

import java.util.Arrays;

public class ArgumentsParser {
    public static void run(String[] args) throws Exception {

        Options options = new Options();

        Option input = new Option("i", "input", true, "main file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output path");
        output.setRequired(false);
        options.addOption(output);

        Option lib = new Option("lib", "library", true, "path to default library");
        output.setRequired(false);
        options.addOption(lib);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String inputFilePath = cmd.getOptionValue("input");
            String outputPath = cmd.getOptionValue("output");
            String[] libPath = cmd.getOptionValues("library");

            if (libPath != null) {
                Arrays.stream(libPath).forEach(Compiler::addDefaultSourceLocation);
            }

            if (outputPath == null)
                outputPath = "out";

            Compiler.compile(inputFilePath, outputPath);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }
}
