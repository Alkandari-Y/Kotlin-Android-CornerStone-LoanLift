package com.coded.loanlift.data.enums

enum class CampaignStatus {
    NEW,                // Status of a new Campaign, users can make edits
    PENDING,            // Status of a campaign under admin review
    REJECTED,           // Isn't approved for launch

    // this is for the public users (not campaign owner)
    ACTIVE,             // Available to users for funding
    FUNDED,             // Reached or surpassed its goal and deadline
    FAILED,             // Didnt reach its goalAmount by deadline
    COMPLETED,          // Finished repayment of funded goal amount plus interest
    DEFAULTED           // Didn't pay back all its funded goal amount plus interest within repayment months
}