export interface TableColumn {
  key: string;
  label: string;
  type?: 'text' | 'currency' | 'date' | 'status' | 'boolean';
}

export interface TableAction {
  id: string;
  label: string;
  tone?: 'primary' | 'secondary' | 'danger';
}

export interface TableActionEvent {
  action: TableAction;
  row: unknown;
}
