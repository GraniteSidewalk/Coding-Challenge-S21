# ACM Research Coding Challenge (Spring 2021) 
This entry was programmed by Thomas Symalla for the ACM Spring 2021 research coding challenge.

# Things used in development:
- Java (with IntelliJ).
- CGView. Repository at https://github.com/paulstothard/cgview . Citation: Stothard P, Wishart DS (2005) Circular genome visualization and exploration using CGView. Bioinformatics 21:537-539.
- Biojava. Source at https://mvnrepository.com/artifact/org.biojava . Citation: BioJava 5: A community driven open-source bioinformatics library
Aleix Lafita, Spencer Bliven, Andreas Prlić, Dmytro Guzenko, Peter W. Rose, Anthony Bradley, Paolo Pavan, Douglas Myers-Turnbull, Yana Valasatava, Michael Heuer, Matt Larson, Stephen K. Burley, & Jose M. Duarte
PLOS Computational Biology (2019) 15 (2):e1006791.

# Notes:
- Simply run "Coding-Challenge-S21.jar" and it will take the file named "Genome.gb" and produce image in the root directory. This file was compiled using intellij.
- Easily expanded for more functionality if so desired.
- CGView is included as a submodule. Shouldn't need to update it.
- Biojava is included as a Maven dependency.

As follows is the original, generic README for the S21 ACM Research Coding Challenge:

## No Collaboration Policy

**You may not collaborate with anyone on this challenge.** You _are_ allowed to use Internet documentation. If you _do_ use existing code (either from Github, Stack Overflow, or other sources), **please cite your sources in the README**.

## Submission Procedure

Please follow the below instructions on how to submit your answers.

1. Create a **public** fork of this repo and name it `ACM-Research-Coding-Challenge-S21`. To fork this repo, click the button on the top right and click the "Fork" button.
2. Clone the fork of the repo to your computer using `git clone [the URL of your clone]`. You may need to install Git for this (Google it).
3. Complete the Challenge based on the instructions below.
4. Submit your solution by filling out this [form](https://acmutd.typeform.com/to/uqAJNXUe).

## Question One

Genome analysis is the identification of genomic features such as gene expression or DNA sequences in an individual's genetic makeup. A genbank file (.gb) format contains information about an individual's DNA sequence. The following dataset in `Genome.gb` contains a complete genome sequence of Tomato Curly Stunt Virus. 

**With this file, create a circular genome map and output it as a JPG/PNG/JPEG format.** We're not looking for any complex maps, just be sure to highlight the features and their labels.

**You may use any programming language you feel most comfortable. We recommend Python because it is the easiest to implement. You're allowed to use any library you want to implement this**, just document which ones you used in this README file. Try to complete this as soon as possible.

Regardless if you can or cannot answer the question, provide a short explanation of how you got your solution or how you think it can be solved in your README.md file. However, we highly recommend giving the challenge a try, you just might learn something new!
