UPDATE report_data SET is_regulated = FALSE WHERE exclusion_reason = 'UN_REGULATED';
UPDATE report_data SET is_regulated = TRUE WHERE exclusion_reason = 'INCLUSION';