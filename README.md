# TransactionMatchingService
Low Level Design System

Problem Statement
Given 2 documents "Buyer.csv" & "Supplier.csv", build a solution that reconciles / matches the txns. The solution should -:
1. categorize the txns into Exact / Partial / Only in Buyer / Only in Supplier categories
2. handle matching of number / string / date

Definitions

Exact Match - 2 elements are an exact match if they are exactly the same
Partial Match - 
    A. Number1, Number2 match partially if Number2 is in range of Number1 +/- threshold
    B. String1, String2 match partially if String2 is similar to String1
    C. Date1, Date2 match partially if Date2 is in range of Date1 +/- threshold
    D. If there are multiple partial matches, the ‘best partial match’ should show up

Notes
    1. Working code is super important
    2. Code quality is table stakes
    3. System Design should be Extensible / Flexible
    4. Can use open source libraries with proper justification

Assumptions
Data Type assumptions
GSTIN, BillNo is String
GST Rate, Taxable Value, IGST, CGST, SGST, Total are double values
For any cell with empty value (number type), 0 value is assumed
Threshold assumptions
Date threshold is taken in form of number of days
For string similarity, threshold value is provided by user
Two strings are similar if s1 can be converted to s2 by total of threshold operations where an operation is insert/delete/replace
Matching assumptions
Matching Priority : Exact > Partial > OnlyInBuyer=OnlyInSupplier
Best Partial Match Criteria - The closest match (out of all partial matches) 


Example

Doc1
Name    RegNum  Salary
Ram     12A3    9000
Shyam   12A4    8500

Doc2
Name    RegNum  Salary
Raam     12A3    9000
Shyam   12A4$    8501

Output
Name    RegNum  Salary  	Category    	Name    RegNum  Salary
Ram     12A3         9000    	Partial     	Raam     12A3    9000
Shyam  12A4       8500    	Partial     	Shyam   12A4$    8501
